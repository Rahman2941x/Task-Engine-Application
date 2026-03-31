package taskengine.taskengine_project_service.dto;

import java.util.List;

public record ValidAndInValidUser(
        List<Long> validUser,
        List<String> validUserEmail,
        List<Long> inValidUser
) {
}
