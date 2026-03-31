package com.taskengine.taskengine_task_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taskengine.taskengine_task_service.model.TaskPriority;
import com.taskengine.taskengine_task_service.model.TaskStatus;
import com.taskengine.taskengine_task_service.model.TaskType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record TaskRequest(
        String taskName,
         String taskDescription,
        TaskType taskType,
        Long projectId,
        String assignedUser,

       TaskPriority priority,
        List<Long> depends_on_task,
        Integer estimatedHours,
        Integer taskOrder,

        @Min(value = 1,message = "plusDays must be greater than 0")
        @Max(value = 30,message = "plusDays must not exceed 30")
        Integer plusDays
) {}

