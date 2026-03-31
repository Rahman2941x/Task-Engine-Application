package com.taskengine.taskengine_task_service.client;

import com.taskengine.taskengine_task_service.configuration.OAuth2FeignConfig;
import com.taskengine.taskengine_task_service.dto.ResponseDTO;
import com.taskengine.taskengine_task_service.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "taskengine-user-service",
        url = "http://localhost:8080",
        configuration = OAuth2FeignConfig.class
)
public interface UserClient {

    @GetMapping("/user/api/v1/get/{email}")
    ResponseDTO<UserDTO> getUserByEmailDtoResponse(@PathVariable("email") String email);

    @GetMapping("/user/get/id/{id}")
    ResponseDTO<UserDTO> getUserById(@PathVariable Long id);

}
