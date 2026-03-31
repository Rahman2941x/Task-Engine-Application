package taskengine.taskengine_project_service.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ProjectResponseDTO(
        Long id,
        String projectName,
        String projectManager,
        List<Long> projectMemberId,
        List<ProjectTaskDTO> taskDTO,
        Boolean isActive,
        LocalDateTime projectDeadLine
) {

}
