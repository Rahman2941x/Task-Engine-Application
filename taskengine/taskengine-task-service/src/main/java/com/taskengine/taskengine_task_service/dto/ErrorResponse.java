package com.taskengine.taskengine_task_service.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String errorMessage
) {
}
