package com.taskengine.taskengine_task_service.exception;

public class TaskIdNotFound extends RuntimeException {

    public TaskIdNotFound(Long id) {
        super("Task id not found: "+id);
    }

}
