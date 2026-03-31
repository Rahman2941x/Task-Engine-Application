package taskengine.taskengine_project_service.dto;

public record IsValidUser(
        Long id,
        String userName,
        Boolean isActive,
        Boolean isValidUser
) {
}
