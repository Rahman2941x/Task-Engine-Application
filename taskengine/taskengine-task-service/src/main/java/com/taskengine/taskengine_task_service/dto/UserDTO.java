package com.taskengine.taskengine_task_service.dto;

public record UserDTO(
        Long id,
        String userName,
        String email,
        String mobileNumber,
        String alternativeNumber,
        String role,
        Boolean isActive
){}

