package com.taskengine.taskengine_user_service.dto;

import jakarta.validation.constraints.NotNull;

public record OAuthClientDTO(
        @NotNull String clientId,
        @NotNull  String clientSecret
) {
}
