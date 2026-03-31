package taskengine.taskengine_project_service.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String errorMessage
) {
    public ErrorResponse(int status,String errorMessage){
        this(LocalDateTime.now(),status,errorMessage);
    }
}
