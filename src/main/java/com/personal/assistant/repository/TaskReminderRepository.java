package com.personal.assistant.repository;

// import java.util.Optional;

// import java.time.format.DateTimeFormatter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.personal.assistant.entity.TaskReminder;

import java.util.Optional;

public interface TaskReminderRepository extends JpaRepository <TaskReminder , String> {
    @Query(
        value = "SELECT * FROM task_reminder t WHERE t.user_id = :userId AND t.task_name = '%:taskName%' ORDER BY t.reminder_time ASC LIMIT 1",
        nativeQuery = true
    )
    Optional<TaskReminder> findByName (@Param("taskName") String taskName, @Param("userId") String userId);

    @Query(
        value = "SELECT * FROM task_reminder t WHERE t.user_id = :userId AND t.reminder_time > NOW() ORDER BY t.reminder_time ASC LIMIT 1",
        nativeQuery = true
    )
    Optional<TaskReminder> findNextActiveTask (@Param("userId") String userId);

    @Query(
        value = "SELECT * FROM task_reminder t WHERE t.task_id = :taskId AND t.reminder_time > NOW() ORDER BY t.reminder_time ASC LIMIT 1",
        nativeQuery = true
    )
    Optional<TaskReminder> findByTaskId (@Param("taskId") String task_id);

}
