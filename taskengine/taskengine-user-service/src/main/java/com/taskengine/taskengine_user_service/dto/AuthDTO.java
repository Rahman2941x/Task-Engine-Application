package com.taskengine.taskengine_user_service.dto;

import jakarta.validation.constraints.NotNull;

public record AuthDTO(
       @NotNull String username,
       @NotNull String password
) {
}
