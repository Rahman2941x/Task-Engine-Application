package com.taskengine.taskengine_user_service.dto;

import com.taskengine.taskengine_user_service.model.User;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ResponseDTO<T> {

    private LocalDateTime localDateTime;
    private int status;
    private T data;

    public ResponseDTO(HttpStatus status, T data){
        this.localDateTime=LocalDateTime.now();
        this.status=status.value();
        this.data=data;
    }

    public ResponseDTO() {
    }

    public ResponseDTO( HttpStatus status, T data, String path) {
        this.localDateTime =LocalDateTime.now();
        this.status = status.value();
        this.data = data;

    }

    public ResponseDTO(T data) {
        this.data=data;
        this.localDateTime=LocalDateTime.now();
    }


    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


//    @Override
//    public String toString() {
//        return "ResponseDTO{" +
//                "localDateTime=" + localDateTime +
//                ", status=" + status +
//                ", data=" + data +
//                ", path='" + path + '\'' +
//                '}';
//    }
}
