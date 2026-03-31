package com.taskengine.taskengine_task_service.dto;

import java.util.List;

public record DependencyRequest(
        List<Long> addDependency,
        List<Long> deleteDependency
) {
}
