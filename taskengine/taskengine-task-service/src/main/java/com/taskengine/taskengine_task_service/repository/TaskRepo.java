package com.taskengine.taskengine_task_service.repository;

import com.taskengine.taskengine_task_service.model.Task;
import com.taskengine.taskengine_task_service.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface TaskRepo extends JpaRepository<Task,Long> {

    @Query(value = "SELECT * FROM task where is_active=true and task_status !='COMPLETED'",nativeQuery = true)
    List<Task> findAllActiveAndNotCompleted();

    List<Task> findByisActiveTrueAndStatusNot(TaskStatus status);

    List<Task> findByisActiveTrueAndStatusNotIn(List<TaskStatus> taskStatus);

    List<Task> findAllByIdInAndIsActiveTrueAndEndDateIsNotNull(List<Long> id);

    List<Task> findAllByIdInAndIsActiveTrue(List<Long> ids);
}
