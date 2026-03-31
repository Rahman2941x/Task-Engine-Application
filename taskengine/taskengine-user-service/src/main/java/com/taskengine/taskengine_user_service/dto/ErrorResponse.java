package com.taskengine.taskengine_user_service.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
        LocalDateTime timeStamp,
        int status,
        String error
) {
}
