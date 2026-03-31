package taskengine.taskengine_project_service.dto;

import java.util.List;

public record ValidAndInValidTask(
        List<ProjectTaskDTO> projectTaskDTOS,
        List<Long> inValidTaskIds
) {
}
