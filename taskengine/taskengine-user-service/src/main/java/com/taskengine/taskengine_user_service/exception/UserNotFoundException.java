package com.taskengine.taskengine_user_service.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {

        super(message);
    }
}
