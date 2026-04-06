package taskengine.taskengine_project_service.dto;

import taskengine.taskengine_project_service.entity.TaskStatus;

public record ProjectTaskDTO(
        Long taskId,
        Long projectId,
        TaskStatus status
) {
}
