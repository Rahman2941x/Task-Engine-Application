package com.taskengine.taskengine_task_service.client;

import com.taskengine.taskengine_task_service.configuration.OAuth2FeignConfig;
import com.taskengine.taskengine_task_service.dto.GetProjectStatusActiveUserTaskDTO;
import com.taskengine.taskengine_task_service.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(
        name = "taskengine-project-service",
        url = "http://localhost:8082",
        configuration = OAuth2FeignConfig.class
)
public interface ProjectClient {

    @GetMapping("/project/api/v1/check-Project/active/user/task/id/{id}")
    public ResponseDTO<GetProjectStatusActiveUserTaskDTO> getProjectStatusActiveUserTask(@PathVariable Long id);


    @PutMapping("/project/api/v1/add-project/task/id/{id}")
    public ResponseDTO<?> addTaskToProject(@RequestBody List<Long> tasks, @PathVariable Long id);
}
