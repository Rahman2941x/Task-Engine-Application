package taskengine.taskengine_project_service.utils;

import taskengine.taskengine_project_service.dto.ProjectResponseDTO;
import taskengine.taskengine_project_service.dto.ProjectTaskDTO;
import taskengine.taskengine_project_service.entity.Project;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ProjectMapper {
    public static ProjectResponseDTO mapProject(Project project){

        List<ProjectTaskDTO> taskDTO= Optional.ofNullable(project.getProjectTask())
                .orElse(Collections.emptyList())
                .stream()
                .map(task->new ProjectTaskDTO(task.getTaskId(),task.getStatus()))
                .toList();

        return new ProjectResponseDTO(
                project.getId(),
                project.getProjectName(),
                project.getProjectManager(),
                project.getProjectMemberId(),
                taskDTO,
                project.getActive(),
                project.getProjectDeadLine()
        );
    }
}
