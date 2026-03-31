package com.taskengine.taskengine_user_service.exception.handler;

import com.taskengine.taskengine_user_service.dto.ErrorResponse;
import com.taskengine.taskengine_user_service.dto.ResponseDTO;
import com.taskengine.taskengine_user_service.exception.BusinessException;
import com.taskengine.taskengine_user_service.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseDTO<String>> handleUserNotFound(UserNotFoundException ex,HttpServletRequest request){
    ResponseDTO<String> responseDTO=new ResponseDTO<>(
            HttpStatus.NOT_FOUND,
            ex.getMessage(),
            request.getRequestURI());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleValidationException(MethodArgumentNotValidException ex){

        Map<String,Object> errors=new HashMap<>();
        for(FieldError error:ex.getBindingResult().getFieldErrors()){
            errors.put(error.getField(),error.getDefaultMessage());
        }

        Map<String,Object> response=Map.of(
                "TimeStamp", LocalDateTime.now(),
                "Status",400,
                "Error",errors
        );
        return ResponseEntity.badRequest().body(response);
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex){
        ErrorResponse response= new ErrorResponse(
                LocalDateTime.now(),
                400,
                "Something went wrong"
        );

        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex){

        ErrorResponse response= new ErrorResponse(LocalDateTime.now(),400, ex.getMessage());

        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }


}
