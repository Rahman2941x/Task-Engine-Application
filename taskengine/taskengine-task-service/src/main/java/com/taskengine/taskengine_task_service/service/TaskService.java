package com.taskengine.taskengine_task_service.service;

import com.taskengine.taskengine_task_service.client.ProjectClient;
import com.taskengine.taskengine_task_service.constant.Constant;
import com.taskengine.taskengine_task_service.algorithm.TopologicalSortAlg;
import com.taskengine.taskengine_task_service.client.UserClient;
import com.taskengine.taskengine_task_service.dto.*;
import com.taskengine.taskengine_task_service.exception.TaskIdNotFound;
import com.taskengine.taskengine_task_service.model.ProjectStatus;
import com.taskengine.taskengine_task_service.model.Task;
import com.taskengine.taskengine_task_service.model.TaskStatus;
import com.taskengine.taskengine_task_service.model.TaskType;
import com.taskengine.taskengine_task_service.repository.TaskRepo;
import com.taskengine.taskengine_task_service.utils.MqTaskProducerUtil;
import com.taskengine.taskengine_task_service.utils.NullCheckUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
    @Autowired
    UserClient userClient;
    @Autowired
    TopologicalSortAlg topologicalSortAlg;


    @Autowired
    ProjectClient projectClient;


    @Autowired
    MqTaskProducerUtil mqTaskProducerUtil;
    @Autowired
    private TaskRepo taskRepo;

    public ResponseEntity<Page<Task>> getAllTaskDetails(int size, int page) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Page<Task> taskPage = taskRepo.findAll(pageable);

        if (taskPage.isEmpty()) {
            throw new RuntimeException(Constant.TABLE_EMPTY);
        }
        return ResponseEntity.ok(taskPage);

    }

    public ResponseEntity<ResponseDTO<?>> checkUserIsActive(String email) {

        logger.info("came");
        ResponseDTO<UserDTO> responseDTO = userClient.getUserByEmailDtoResponse(email);
        logger.info("");

        UserDTO user = responseDTO.data();

        if (user.isActive()) {
            ResponseDTO<UserDTO> newResponseDTO = new ResponseDTO<>(
                    HttpStatus.OK.value(), user);
            return ResponseEntity.ok(newResponseDTO);
        } else {
            ResponseDTO<String> newResponseDTO = new ResponseDTO<>(
                    HttpStatus.EXPECTATION_FAILED.value(), Constant.USER_NOT_ACTIVE + user.email());
            return ResponseEntity.ok(newResponseDTO);
        }
    }

    @Transactional
    public ResponseEntity<ResponseDTO<?>> createTask(TaskRequest request) {

        ResponseDTO<GetProjectStatusActiveUserTaskDTO> projectStatusActiveUserTask = projectClient.getProjectStatusActiveUserTask(request.projectId());
        GetProjectStatusActiveUserTaskDTO projectData = projectStatusActiveUserTask.data();

        ResponseDTO<UserDTO> userDto = userClient.getUserByEmailDtoResponse(request.assignedUser());


        Task task = new Task();
        task.setName(request.taskName());
        task.setDescription(request.taskDescription());
        task.setTaskType(request.taskType());

        Long projectId = null;

        if (request.taskType() == TaskType.PROJECT) {
            if (projectData == null) {
                throw new RuntimeException("Project not found");
            }
            if (Boolean.FALSE.equals(projectData.isActive())) {
                throw new RuntimeException("Project is Not Active");
            }
            if (projectData.status().equals(ProjectStatus.COMPLETED)) {
                throw new RuntimeException("Cannot create task under completed project");
            }
            projectId = request.projectId();
        }
        task.setProjectId(projectId);

        String assignedUser = null;
        String loggedInUser = loginUser();

        if (request.taskType() == TaskType.PROJECT) {
            Long userId = userDto.data().id();
            if (projectData.user() == null) {
                throw new RuntimeException("Project data is invalid");
            }

            if (!projectData.user().contains(userId)) {
                throw new RuntimeException("User is not part of the project");
            }
            assignedUser = request.assignedUser();
        } else {
            assignedUser = loggedInUser;
        }
        task.setPriority(request.priority());
        task.setDependencies(request.depends_on_task());
        task.setEstimatedHours(request.estimatedHours());
        task.setTaskOrder(request.taskOrder());

        task.setAssignedUser(assignedUser);
        task.setStatus(TaskStatus.CREATED);
        task.setUserAccepted(false);

        task.setDueDate(LocalDateTime.now().plusDays(request.plusDays()));

        task.setCreatedAt(LocalDateTime.now());
        task.setCreatedBy(loggedInUser);

        taskRepo.save(task);

        logger.info("newly generated Task id:: {} for task name :: {}", task.getId(), task.getName());
        List<Long> taskId = List.of(task.getId());

        projectClient.addTaskToProject(taskId, task.getProjectId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO<>(LocalDateTime.now(),
                        HttpStatus.CREATED.value(),
                        task));
    }

    private String loginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getName().equals("anonymousUser"))
            throw new RuntimeException(Constant.USER_AUTHENTICATED);

        return authentication.getName();
    }

    public ResponseEntity<ResponseDTO<?>> getTaskById(Long id) {
        Task task = taskRepo.findById(id).orElseThrow(() -> new TaskIdNotFound(id));

        if (!task.getActive()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(new ResponseDTO<>(LocalDateTime.now(), HttpStatus.NOT_ACCEPTABLE.value(), Constant.TASK_INACTIVE));
        }
        return ResponseEntity.ok(new ResponseDTO<>(LocalDateTime.now(), HttpStatus.OK.value(), task));
    }

    public ResponseEntity<ResponseDTO<String>> setActivation(Boolean isActive, Long id) {

        Task task = taskRepo.findById(id).orElseThrow(() -> new TaskIdNotFound(id));

        if (task.getActive().equals(isActive)) {
            String message = isActive ? Constant.TASK_ACTIVE : Constant.TASK_INACTIVE;

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(LocalDateTime.now(), 400, message));
        }

        task.setActive(isActive);
        taskRepo.save(task);

        String message = Boolean.TRUE.equals(isActive) ? Constant.TASK_ACTIVATED : Constant.TASK_DEACTIVATED;

        return ResponseEntity.ok(new ResponseDTO<>(LocalDateTime.now(), HttpStatus.OK.value(), message));
    }

    @Transactional
    public ResponseEntity<ResponseDTO<?>> claimTask(Long id) {
        Task task = taskRepo.findById(id).orElseThrow(() -> new TaskIdNotFound(id));

        if (!Boolean.TRUE.equals(task.getActive())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(400, Constant.TASK_INACTIVE));
        }

        String loginUser = loginUser();
        if (task.getAssignedUser()!=null && task.getAssignedUser().equals(loginUser)) {
            task.setStatus(TaskStatus.INPROGRESS);
            taskRepo.save(task);
            List<Long> taskId = List.of(task.getId());
            projectClient.addTaskToProject(taskId, task.getProjectId());

            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(new ResponseDTO<>(HttpStatus.NOT_ACCEPTABLE.value(), Constant.TASK_ALREADY_ASSIGNED + task.getAssignedUser()));
        }

        task.setAssignedUser(loginUser);
        task.setStatus(TaskStatus.INPROGRESS);
        task.setUserAccepted(true);
        taskRepo.save(task);

        List<Long> taskId = List.of(task.getId());

        projectClient.addTaskToProject(taskId, task.getProjectId());
        ProjectTaskDTO projectTaskDTO = new ProjectTaskDTO(task.getId(), task.getProjectId(), task.getStatus());

        //SendTaskStatusToProject
        if (task.getId() != null && task.getProjectId() != null && task.getStatus() != null) {
            mqTaskProducerUtil.sendTaskToProject(projectTaskDTO);
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseDTO<>(HttpStatus.ACCEPTED.value(), Constant.TASK_CLAIMED + loginUser()));

    }

    public ResponseEntity<ResponseDTO<?>> assignedTo(Long id, String email) {

        Task task = taskRepo.findById(id).orElseThrow(() -> new TaskIdNotFound(id));
        if (!Boolean.TRUE.equals(task.getActive())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(400, Constant.TASK_INACTIVE));
        }
        if (email == null || email.isBlank()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), Constant.USER_NOT_BLANK));
        }
        if (task.getAssignedUser().equals(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseDTO<>(HttpStatus.CONFLICT.value(), Constant.TASK_ALREADY_ASSIGNED + email));
        }
        task.setAssignedUser(email);
        taskRepo.save(task);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseDTO<>(HttpStatus.ACCEPTED.value(), Constant.TASK_ASSIGNED + email));


    }

    public ResponseEntity<ResponseDTO<?>> acceptTask(Long id) {
        Task task = taskRepo.findById(id).orElseThrow(() -> new TaskIdNotFound(id));

        String loginUser = loginUser();

        if (!task.getActive()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseDTO<>(HttpStatus.CONFLICT.value(), Constant.TASK_INACTIVE));
        }

        if (task.getAssignedUser() == null) {
            return claimTask(id);
        }

        if (loginUser.equals(task.getAssignedUser())) {
            if (Boolean.TRUE.equals(task.getUserAccepted())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ResponseDTO<>(HttpStatus.CONFLICT.value(), Constant.TASK_ALREADY_ACCEPETED + task.getAssignedUser()));
            }

            task.setStatus(TaskStatus.INPROGRESS);
            task.setUserAccepted(true);
            taskRepo.save(task);

            List<Long> taskId = List.of(task.getId());

            projectClient.addTaskToProject(taskId, task.getProjectId());

            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(new ResponseDTO<>(HttpStatus.ACCEPTED.value(), Constant.TASK_ACCEPETED_BY + loginUser));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO<>(400, Constant.TASK_ALREADY_ACCEPETED + task.getAssignedUser()));
    }

    public ResponseEntity<ResponseDTO<?>> updateStatus(Long id, TaskStatusDTO taskStatus) {
        Task task = taskRepo.findById(id).orElseThrow(() -> new TaskIdNotFound(id));

        String loginUser = loginUser();
        TaskStatus newStatus = taskStatus.status();

        if (!loginUser.equals(task.getAssignedUser()) || !loginUser.equals(task.getCreatedBy())) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>(400, Constant.ASSIGNED_CREATED_USER_CAN_EDIT));
        }
        if (!task.getActive()) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>(400, Constant.TASK_INACTIVE));
        }
        if (task.getStatus() == TaskStatus.CANCELLED) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>(400, Constant.TASK_CANCELLED));
        }
        if (task.getStatus() == TaskStatus.CREATED) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>(400, Constant.TASK_CLAIMED_OR_ACCEPTED));
        }
        if (newStatus == TaskStatus.CREATED) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>(400, Constant.STATUS_NOT_BACK_TO_CREATED));
        }
        if (newStatus == TaskStatus.BLOCKED) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>(400, Constant.TASK_BLOCKED));
        }
        if (task.getStatus() == TaskStatus.COMPLETED &&
                newStatus != TaskStatus.REOPENED) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>(400, Constant.COMPLETED_TO_REOPEN));
        }

        task.setStatus(newStatus);
        //if(!Objects.isNull(taskStatus.remark()) && !taskStatus.remark().isBlank()) task.setRemark(taskStatus.remark());
        if (StringUtils.hasText(taskStatus.remark())) task.setRemark(taskStatus.remark());
        task.setUpdatedBy(loginUser);
        taskRepo.save(task);
        List<Long> taskId = List.of(task.getId());

        projectClient.addTaskToProject(taskId, task.getProjectId());
        ProjectTaskDTO projectTaskDTO = new ProjectTaskDTO(task.getId(), task.getProjectId(), task.getStatus());

        //SendTaskStatusToProject
        if (task.getId() != null && task.getProjectId() != null && task.getStatus() != null) {
            mqTaskProducerUtil.sendTaskToProject(projectTaskDTO);
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new ResponseDTO<>(HttpStatus.ACCEPTED.value(), Constant.TASK_STATUS + newStatus));
    }

    public ResponseEntity<ResponseDTO<?>> updateRemark(Long id, RemarkDTO remarkDTO) {
        Task task = taskRepo.findById(id).orElseThrow(() -> new TaskIdNotFound(id));
        String loginUser = loginUser();
        String remark = remarkDTO.remark();

        boolean isCreator = loginUser.equalsIgnoreCase(task.getCreatedBy());
        boolean isAssignee = task.getAssignedUser() != null && loginUser.equalsIgnoreCase(task.getAssignedUser());
        boolean isCancelled = task.getStatus().equals(TaskStatus.CANCELLED);

        if (!task.getActive()) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>(400, Constant.TASK_INACTIVE));
        }

        if (isCreator || isAssignee) {
            if (!isCancelled) {
                if (StringUtils.hasText(remark)) {
                    task.setRemark(remark);
                    task.setUpdatedBy(loginUser);
                    taskRepo.save(task);

                    return ResponseEntity.status(HttpStatus.ACCEPTED)
                            .body(new ResponseDTO<>(HttpStatus.ACCEPTED.value(), Constant.REMARK_UPDATED + loginUser));
                }
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                        .body(new ResponseDTO<>(HttpStatus.NOT_ACCEPTABLE.value(), Constant.REMARK_BLANK));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), Constant.CANCELLED_TASK_CANT_EDIT));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), Constant.ASSIGNED_CREATED_USER_CAN_EDIT));
    }

    public ResponseEntity<ResponseDTO<?>> updateTaskDetail(Long id, TaskRequest request) {


        if (request == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), Constant.INVALID_ATTRIBUTES));
        }


        Task task = taskRepo.findById(id).orElseThrow(() -> new TaskIdNotFound(id));

        if (!task.getActive()) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>(400, Constant.TASK_INACTIVE));
        }

        String loginUser = loginUser();
        boolean isCancelled = task.getStatus() == TaskStatus.CANCELLED;
        boolean isCreator = loginUser.equalsIgnoreCase(task.getCreatedBy());


        if (isCancelled) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), Constant.CANCELLED_TASK_CANT_EDIT));
        }
        if (!isCreator) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), Constant.TASK_CREATOR_CAN_EDIT));
        }


        BeanUtils.copyProperties(request, task, NullCheckUtil.getNullPropertyNames(request));

        if (request.depends_on_task() != null) {
            task.setDependencies(request.depends_on_task());
        }

        if (TaskType.PROJECT.equals(request.taskType())) {

            // TODO validate project id in project service
            task.setProjectId(request.projectId());

            // TODO validate project member from project service
            task.setAssignedUser(request.assignedUser());

        }

        task.setUpdatedBy(loginUser);
        taskRepo.save(task);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new ResponseDTO<>(HttpStatus.ACCEPTED.value(), Constant.UPDATED));

    }

    public ResponseEntity<ResponseDTO<String>> getTaskStatus(Long id) {
        Task task = taskRepo.findById(id).orElseThrow(() -> new TaskIdNotFound(id));

        if (!task.getActive()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), Constant.TASK_INACTIVE));
        }

        if(!task.getDependencies().isEmpty()){
            TaskStatus status = evaluateTaskStatus(task);
            task.setStatus(status);
            taskRepo.save(task);
        }


        if (task.getStatus() == TaskStatus.BLOCKED && !task.getDependencies().isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseDTO<>(HttpStatus.CONFLICT.value(), Constant.TASK_BLOCKED + task.getDependencies()));
        }

        return ResponseEntity.status(200).body(new ResponseDTO<>(200, Constant.TASK_STATUS + task.getStatus()));
    }

    public TaskStatus evaluateTaskStatus(Task task) {
        if (task.getDependencies() == null || task.getDependencies().isEmpty()) {
            if (task.getStatus() == TaskStatus.BLOCKED) {
                return TaskStatus.READY;
            }

        }

        boolean allTaskCompleted = true;

        if (task.getDependencies() != null) {
            for (Long dependTaskId : task.getDependencies()) {
                Task dependTask = taskRepo.findById(dependTaskId).orElseThrow(
                        () -> new TaskIdNotFound(dependTaskId));
                if (dependTask.getStatus() != TaskStatus.COMPLETED) {
                    allTaskCompleted = false;
                    break;
                }
            }
        }

        if (allTaskCompleted) {
            task.setStatus(TaskStatus.READY);
            return TaskStatus.READY;
        } else {
            task.setStatus(TaskStatus.BLOCKED);
            return TaskStatus.BLOCKED;
        }

    }

    public ResponseEntity<ResponseDTO<?>> getTaskExecutionPriorityOrder() {
        return taskScheduler(true);
    }

    public ResponseEntity<ResponseDTO<?>> getTaskExecutionOrder() {
        return taskScheduler(false);
    }

    public ResponseEntity<ResponseDTO<?>> taskScheduler(boolean usePriority) {

        List<Task> tasks = taskRepo.findByisActiveTrueAndStatusNot(TaskStatus.COMPLETED);
        List<TaskStatus> exclude = List.of(TaskStatus.COMPLETED, TaskStatus.READY);
        List<Task> taskDep = taskRepo.findByisActiveTrueAndStatusNotIn(exclude);


        boolean hasDependent = tasks.stream()
                .anyMatch(task -> task.getDependencies() != null && !task.getDependencies().isEmpty());

        if (hasDependent) {
            List<Long> executionOrder = topologicalSortAlg.topologicalSort(tasks, usePriority);
            if (executionOrder.size() != taskDep.size()) {
                logger.info("Execution Order size:: {}", executionOrder.size());
                logger.info("Tasks size:: {}", taskDep.size());
                logger.info("Task id {}", tasks);

                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ResponseDTO<>(HttpStatus.CONFLICT.value(), Constant.CIRCULAR_DEPENDENCY));
            }
            return ResponseEntity.ok(
                    new ResponseDTO<>(HttpStatus.OK.value(), executionOrder));
        }

        List<Long> priorityOrder = tasks.stream()
                .sorted(Comparator.comparing(Task::getPriority).reversed())
                .map(Task::getId)
                .toList();

        return ResponseEntity.status(200)
                .body(new ResponseDTO<>(200, Constant.NO_DEPENDENCY));
    }


    public ResponseEntity<ResponseDTO<?>> updateTaskDependency(Long id, DependencyRequest dependencyList) {
        Task task = taskRepo.findById(id).orElseThrow(() -> new TaskIdNotFound(id));

        if (!task.getActive()) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>(400, Constant.TASK_INACTIVE));
        }

        List<Long> dependencies = task.getDependencies();
        List<Long> deleteDep = dependencyList.deleteDependency();
        List<Long> addDep = dependencyList.addDependency();

        if (addDep.isEmpty() && deleteDep.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ResponseDTO<>(HttpStatus.NO_CONTENT.value(), Constant.DEPENDENCY_BLANK));
        }

        if (dependencies == null || dependencies.isEmpty()) {
            dependencies = new ArrayList<>();
        }

        dependencies.removeAll(deleteDep);

        for (Long dep : addDep) {
            if (!dependencies.contains(dep)) {
                dependencies.add(dep);
            }
        }

        task.setDependencies(dependencies);

        return ResponseEntity.ok(
                new ResponseDTO<>(HttpStatus.OK.value(),
                        Constant.DEPENDENCY_UPDATED + dependencies));
    }


    public ResponseDTO<ProjectTaskResponse> getProjectTaskList(List<Long> ids) {
        List<Task> tasks = taskRepo.findAllByIdInAndIsActiveTrue(ids);

        Map<Long, Task> taskMap = tasks.stream().collect(Collectors.toMap(
                Task::getId,
                Function.identity()
        ));

        List<Long> inValidTaskIds = ids
                .stream()
                .distinct()
                .filter(id -> !taskMap.containsKey(id))
                .toList();

        List<ProjectTaskDTO> projectTask = tasks.stream()
                .map(task -> new ProjectTaskDTO(
                        task.getId(),
                        task.getProjectId(),
                        task.getStatus()
                )).toList();

        ProjectTaskResponse projectTaskResponse = new ProjectTaskResponse(projectTask, inValidTaskIds);

        return new ResponseDTO<>(HttpStatus.OK.value(), projectTaskResponse);
    }

    public TaskRepo getTaskRepo() {
        return taskRepo;
    }
}

