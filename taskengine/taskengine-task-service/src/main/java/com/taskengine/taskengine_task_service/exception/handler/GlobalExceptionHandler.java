package com.taskengine.taskengine_task_service.exception.handler;

import com.taskengine.taskengine_task_service.dto.ErrorResponse;
import com.taskengine.taskengine_task_service.exception.BusinessException;
import com.taskengine.taskengine_task_service.exception.TaskIdNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex){
        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                400,
                ex.getMessage()
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {

        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Something went wrong" + ex.getMessage()
        );

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TaskIdNotFound.class)
    public ResponseEntity<ErrorResponse> handleTaskIdNotFound(TaskIdNotFound ex){
        ErrorResponse response=new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

    }
}
