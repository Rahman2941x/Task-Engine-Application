package taskengine.taskengine_project_service.service;

import lombok.Lombok;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import taskengine.taskengine_project_service.client.TaskClient;
import taskengine.taskengine_project_service.client.UserClient;
import taskengine.taskengine_project_service.constant.CommanConstant;
import taskengine.taskengine_project_service.dto.*;
import taskengine.taskengine_project_service.entity.Project;
import taskengine.taskengine_project_service.entity.ProjectStatus;
import taskengine.taskengine_project_service.entity.ProjectTask;
import taskengine.taskengine_project_service.entity.TaskStatus;
import taskengine.taskengine_project_service.repository.ProjectRepo;
import taskengine.taskengine_project_service.utils.ProjectMapper;

import javax.swing.*;
import java.util.*;
import java.util.logging.Handler;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final static Logger logger = LoggerFactory.getLogger(ProjectService.class);
    @Autowired
    UserClient userClient;

    @Autowired
    TaskClient taskClient;
    @Autowired
    private ProjectRepo projectRepo;

    public ResponseEntity<ResponseDTO<?>> createProject(ProjectRequestDTO projectRequest) {

        Project project = new Project();

        Boolean projectExist = projectRepo.existsByProjectNameAndProjectManager(
                projectRequest.projectName(), projectRequest.projectManager()
        );
        if (projectExist) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), CommanConstant.PROJECT_ALREADY_EXIST));
        }


        project.setProjectName(projectRequest.projectName());
        project.setProjectManager(projectRequest.projectManager());


        if (projectRequest.projectMemberId() != null &&
                !projectRequest.projectMemberId().isEmpty()) {

            ValidAndInValidUser users = validateUser(projectRequest.projectMemberId());

            if (!users.inValidUser().isEmpty()) {
                logger.error("Invalid User:: {}", users.inValidUser());
            }
            project.setProjectMemberId(users.validUser());
        }

        if (projectRequest.projectTask() != null &&
                !projectRequest.projectTask().isEmpty()) {

            List<ProjectTask> projectTasks = getProjectTasks(projectRequest.projectMemberId(), project);

            project.setProjectTask(projectTasks);
        }


        Project savedProject = projectRepo.save(project);
        ProjectResponseDTO responseDTO = ProjectMapper.mapProject(savedProject);


        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO<>(
                        HttpStatus.CREATED.value()
                        , responseDTO
                ));
    }

    private ValidAndInValidTask validateTask(List<Long> ids) {
        ResponseDTO<ValidAndInValidTask> tasks = taskClient.projectTaskList(ids);
        if (tasks == null || tasks.data() == null) {
            throw new RuntimeException("Invalid response from Task Service");
        }

        if (tasks.data().inValidTaskIds() != null && !tasks.data().inValidTaskIds().isEmpty()) {
            logger.error("Invalid task id::{}", tasks.data().inValidTaskIds());
        }

        return tasks.data();
    }

    public ValidAndInValidUser validateUser(List<Long> projectMemberId) {
        logger.info("invoke client");
        ResponseDTO<List<IsValidUser>> body = userClient.
                validateUser(projectMemberId);
        logger.info("came out client");

        if (body == null || body.data() == null) {
            throw new RuntimeException("Invalid response from User Service");
        }

        List<Long> inValidUser = body.data()
                .stream()
                .filter(valid -> Boolean.FALSE.equals(valid.isValidUser()))
                .map(IsValidUser::id)
                .toList();

        List<Long> validUser = body.data()
                .stream()
                .filter(IsValidUser::isValidUser)
                .map(IsValidUser::id)
                .toList();
        List<String> validUserEmail=body.data()
                .stream()
                .filter(IsValidUser::isValidUser)
                .map(IsValidUser::userName)
                .toList();


        return new ValidAndInValidUser(validUser,validUserEmail,inValidUser);
    }


    public ResponseEntity<ResponseDTO<?>> getProjectById(Long id) {
        Project project = projectRepo.findById(id).orElseThrow(() -> new RuntimeException("Project id not found::" + id));

        ProjectResponseDTO responseDTO = ProjectMapper.mapProject(project);

        if (!project.getActive()) {
            return ResponseEntity.ok(new ResponseDTO<>(200,
                    "project is not Active Project id" + id));
        }
        return ResponseEntity.ok(new ResponseDTO<>(200, responseDTO));
    }

    public ResponseEntity<ResponseDTO<?>> addUserToProject(List<Long> newUser, Long id) {
        Project project = projectRepo.findById(id).orElseThrow(() -> new RuntimeException("Project id not found::" + id));

        if (newUser == null || newUser.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(),
                            "User List is Empty"));
        }
        ValidAndInValidUser validUser = validateUser(newUser);

        if (!validUser.inValidUser().isEmpty()) {
            logger.error("Invalid user list {}", validUser.inValidUser());
        }

        List<Long> existingUser = project.getProjectMemberId();

        if (existingUser == null || existingUser.isEmpty()) {
            existingUser = new ArrayList<>();
        }
        Set<Long> finalExistingUser = new HashSet<>(existingUser);


        Set<Long> newValidUser = validUser.validUser().stream().filter(user -> !finalExistingUser.contains(user))
                .collect(Collectors.toSet());


        if (newValidUser.isEmpty()) {
            return ResponseEntity
                    .ok(new ResponseDTO<>(HttpStatus.OK.value(), "User Already available in project"));
        }

        finalExistingUser.addAll(newValidUser);

        project.setProjectMemberId(
                finalExistingUser.stream()
                        .distinct()
                        .collect(Collectors.toList())
        );

        projectRepo.save(project);

        return ResponseEntity
                .ok(new ResponseDTO<>(HttpStatus.OK.value(),
                        "New User has been added " + finalExistingUser));


    }

    private List<ProjectTask> getProjectTasks(List<Long> taskList, Project project) {
        ValidAndInValidTask tasks = validateTask(taskList);

        return tasks.projectTaskDTOS()
                .stream()
                .map(task -> {
                    ProjectTask pt = new ProjectTask();
                    pt.setTaskId(task.taskId());
                    pt.setStatus(task.status());
                    pt.setProject(project);
                    return pt;
                }).toList();

    }

    public ResponseDTO<?> addTaskToProject(Long id, List<Long> tasks) {
        Project project = projectRepo.findById(id).orElseThrow(() -> new RuntimeException("Project id not found::" + id));

        if (tasks == null || tasks.isEmpty()) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(),
                            "Task List is Empty");
        }

        List<ProjectTask> existingProjectTasks = project.getProjectTask();

        if (existingProjectTasks == null) {
            existingProjectTasks = new ArrayList<>();
        }

        Set<Long> existingTaskId = existingProjectTasks
                .stream()
                .map(ProjectTask::getTaskId)
                .collect(Collectors.toSet());

        List<ProjectTask> projectTasks = getProjectTasks(tasks, project);

        List<ProjectTask> newTasks = projectTasks.stream()
                .filter(task -> !existingTaskId.contains(task.getTaskId()))
                .toList();

        if (!newTasks.isEmpty()) {
            existingProjectTasks.addAll(newTasks);
            project.setProjectTask(existingProjectTasks);
            projectRepo.save(project);

            return new ResponseDTO<>(HttpStatus.OK.value(),
                            "new Task has been added to project:: " + tasks);
        }
        return new ResponseDTO<>(HttpStatus.OK.value(),
                        "Task Already available in project");

    }

    public ResponseDTO<GetProjectStatusActiveUserTaskDTO> getStatusActiveUserTask(Long id) {
        Project project = projectRepo.findById(id).orElseThrow(() -> new RuntimeException("Project id not found::" + id));

        GetProjectStatusActiveUserTaskDTO responseDTO= new GetProjectStatusActiveUserTaskDTO(
                project.getProjectStatus(),
                project.getActive(),
                project.getProjectMemberId()!=null?project.getProjectMemberId():Collections.emptyList(),
                project.getProjectTask()!=null?project.getProjectTask().stream()
                        .map(ProjectTask::getTaskId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
                        :Collections.emptyList()

        );

        return new ResponseDTO<>(200,responseDTO);
    }

    public ResponseDTO<?> deleteProjectById(Long id) {
        Project project = projectRepo.findById(id).orElseThrow(() -> new RuntimeException("Project id not found::" + id));

        projectRepo.deleteById(id);



        return new ResponseDTO<>(200,"Project has been deleted");
    }
}
