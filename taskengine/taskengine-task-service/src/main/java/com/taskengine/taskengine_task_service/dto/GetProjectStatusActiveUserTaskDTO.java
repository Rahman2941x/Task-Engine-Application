package com.taskengine.taskengine_task_service.dto;

import com.taskengine.taskengine_task_service.model.ProjectStatus;

import java.util.List;

public record GetProjectStatusActiveUserTaskDTO(
        ProjectStatus status,
        Boolean isActive,
        List<Long> user,
        List<Long> task
) {
}
