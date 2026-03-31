package com.taskengine.taskengine_task_service.dto;

import java.time.LocalDateTime;

public record ResponseDTO<T>(
         LocalDateTime localDateTime,
         int status,
         T data
) {
    public ResponseDTO(int status,T data){
        this(LocalDateTime.now(),status,data);
    }
}
