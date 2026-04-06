package taskengine.taskengine_project_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskengine.taskengine_project_service.dto.*;
import taskengine.taskengine_project_service.entity.Project;
import taskengine.taskengine_project_service.service.ProjectService;

import java.util.List;

@RestController()
@RequestMapping("/project/api/v1")
public class ProjectController {


    @Autowired
    ProjectService service;

    @PostMapping("/create-project")
    public ResponseEntity<ResponseDTO<?>> createProject(@RequestBody ProjectRequestDTO request){
        return service.createProject(request);
    }

    @GetMapping("/validate-project-user")
    public ValidAndInValidUser ValidUserList(@RequestBody List<Long> ids){
        return service.validateUser(ids);
    }

    @GetMapping("/get-project/id/{id}")
    public ResponseEntity<ResponseDTO<?>> getProjectById(@PathVariable Long id){
        return service.getProjectById(id);
    }

    @PatchMapping("/add-project/member/id/{id}")
    public ResponseEntity<ResponseDTO<?>> addMemberProject(@RequestBody List<Long> newUser,@PathVariable Long id){
        return service.addUserToProject(newUser,id);
    }

    @PutMapping("/add-project/task/id/{id}")
    public ResponseDTO<?> addTaskToProject(@RequestBody List<Long> tasks,@PathVariable Long id){
        return service.addTaskToProject(id,tasks);
    }

    @GetMapping("/check-Project/active/user/task/id/{id}")
    public ResponseDTO<GetProjectStatusActiveUserTaskDTO> getProjectStatusActiveUserTask(@PathVariable Long id){
        return service.getStatusActiveUserTask(id);
    }


}
