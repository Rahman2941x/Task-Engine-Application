package com.taskengine.taskengine_task_service.controller;

import com.taskengine.taskengine_task_service.client.UserClient;
import com.taskengine.taskengine_task_service.dto.*;
import com.taskengine.taskengine_task_service.model.Task;
import com.taskengine.taskengine_task_service.model.TaskType;
import com.taskengine.taskengine_task_service.service.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import jakarta.ws.rs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("task/api/v1")
@RestController
public class TaskController {


    @Autowired
    private TaskService taskService;

    @GetMapping("/get/tasks")
    public ResponseEntity<Page<Task>> getTasks(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size){
        return taskService.getAllTaskDetails(size,page);
    }

    @GetMapping("/check-user-active/{email}")
    public  ResponseEntity<ResponseDTO<?>> getUserByEmailAndCheckIsActive(@PathVariable("email") String email){
        return taskService.checkUserIsActive(email);
    }

    @PostMapping("/create-task")
    public ResponseEntity<ResponseDTO<?>> createTask(@RequestBody  @Valid TaskRequest request){
        return  taskService.createTask(request);
    }

    @GetMapping("/get-task/{id}")
    public  ResponseEntity<ResponseDTO<?>> getTaskById(@PathVariable Long id){
        return taskService.getTaskById(id);
    }

    @PatchMapping("/activation/{isActive}/id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<String>> setActivation(@PathVariable Boolean isActive,@PathVariable Long id){
        return taskService.setActivation(isActive,id);
    }

    @PatchMapping("/claim/task/id/{id}")
    public ResponseEntity<ResponseDTO<?>> claimTask(@PathVariable Long id){
        return taskService.claimTask(id);
    }

    @PatchMapping("/assign/id/{id}/email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<?>> AssignedTo(@PathVariable Long id,@PathVariable String email){
        return taskService.assignedTo(id,email);
    }

    @PatchMapping("/accept/task/id/{id}")
    public ResponseEntity<ResponseDTO<?>> acceptTask(@PathVariable Long id){
        return taskService.acceptTask(id);
    }

    @PatchMapping("/update/status/id/{id}")
    public ResponseEntity<ResponseDTO<?>> updateStatus(@PathVariable Long id,@RequestBody TaskStatusDTO statusUpdate){
        return taskService.updateStatus(id,statusUpdate);
    }

    @PatchMapping("/update/remark/id/{id}")
    public ResponseEntity<ResponseDTO<?>> updateRemark(@PathVariable Long id,@RequestBody RemarkDTO remark){
        return taskService.updateRemark(id,remark);
    }

    @PatchMapping("/update-task_detail/id/{id}")
    public ResponseEntity<ResponseDTO<?>> updateTaskDetail(@PathVariable Long id, @Valid @RequestBody TaskRequest request){
        return taskService.updateTaskDetail(id,request);
    }

    @GetMapping("/check-status/id/{id}")
    public ResponseEntity<ResponseDTO<String>> getTaskStatus(@PathVariable Long id){
        return taskService.getTaskStatus(id);
    }

    @GetMapping("/task-execution-order")
    public ResponseEntity<ResponseDTO<?>> getExecutionOrder(){
        return taskService.getTaskExecutionOrder();
    }

    @GetMapping("/task-execution-order/priority")
    public ResponseEntity<ResponseDTO<?>> getExecutionPriorityOrder(){
        return taskService.getTaskExecutionPriorityOrder();
    }

    @PatchMapping("/Update-Dependency/id/{id}")
    public ResponseEntity<ResponseDTO<?>> updateTaskDependency(@PathVariable  Long id,@RequestBody DependencyRequest dependency){
        return taskService.updateTaskDependency(id,dependency);
    }

    @PostMapping("/project-task-list")
    public ResponseDTO<ProjectTaskResponse> projectTaskList(@RequestBody List<Long> ids){
        return taskService.getProjectTaskList(ids);
    }

}
