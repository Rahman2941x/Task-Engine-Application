package com.taskengine.taskengine_user_service.dto;

public record IsValidUser(
        Long id,
        String userName,
        Boolean isActive,
        Boolean isValidUser
) {

}
