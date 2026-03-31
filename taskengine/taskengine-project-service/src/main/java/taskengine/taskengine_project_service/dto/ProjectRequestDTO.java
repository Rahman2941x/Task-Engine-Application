package taskengine.taskengine_project_service.dto;

import taskengine.taskengine_project_service.entity.ProjectStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record ProjectRequestDTO(
        String projectName,
        String projectManager,
        List<Long> projectMemberId,
        List<Long> projectTask
) {
}
