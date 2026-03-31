package com.taskengine.taskengine_task_service.dto;

import org.apache.catalina.LifecycleState;

import java.util.List;

public record ProjectTaskResponse(
        List<ProjectTaskDTO> projectTaskDTOS,
        List<Long> inValidTaskIds
) {
}
