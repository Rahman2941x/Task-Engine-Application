package taskengine.taskengine_project_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import taskengine.taskengine_project_service.dto.ErrorResponse;
import taskengine.taskengine_project_service.dto.ResponseDTO;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public ResponseEntity<ErrorResponse> handleException(Exception ex){
        ErrorResponse response =new ErrorResponse(
                HttpStatus.EXPECTATION_FAILED.value(),
                "Something went wrong" +"\n"+ex.getMessage()
        );


        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
    }

}
