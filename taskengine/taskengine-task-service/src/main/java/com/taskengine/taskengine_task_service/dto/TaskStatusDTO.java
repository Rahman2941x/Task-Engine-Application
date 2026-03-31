package com.taskengine.taskengine_task_service.dto;

import com.taskengine.taskengine_task_service.model.TaskStatus;

public record TaskStatusDTO(
        TaskStatus status,
        String remark
) {
}
