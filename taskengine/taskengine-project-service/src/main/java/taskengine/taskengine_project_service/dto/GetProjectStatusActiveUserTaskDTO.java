package taskengine.taskengine_project_service.dto;

import taskengine.taskengine_project_service.entity.ProjectStatus;

import java.util.List;

public record GetProjectStatusActiveUserTaskDTO(
        ProjectStatus status,
        Boolean isActive,
        List<Long> user,
        List<Long> task
) {
}
