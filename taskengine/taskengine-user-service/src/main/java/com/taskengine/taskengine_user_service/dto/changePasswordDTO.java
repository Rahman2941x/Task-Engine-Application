package com.taskengine.taskengine_user_service.dto;

import jakarta.annotation.Nonnull;

public record changePasswordDTO(@Nonnull String newPassword,@Nonnull String oldPassword) {
}
