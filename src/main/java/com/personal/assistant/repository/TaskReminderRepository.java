package com.personal.assistant.repository;

// import java.util.Optional;

// import java.time.format.DateTimeFormatter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.personal.assistant.entity.TaskReminder;

public interface TaskReminderRepository extends JpaRepository <TaskReminder , String> {
    @Query(
        value = "SELECT * FROM task_reminder t WHERE t.user_id = :userId AND t.task_name = '%:taskName%' ORDER BY t.time ASC LIMIT 1",
        nativeQuery = true
    )
    TaskReminder findByName (@Param("taskName") String taskName, @Param("userId") String userId);

    @Query(
        value = "SELECT * FROM task_reminder t WHERE t.user_id = :userId AND t.time > NOW() ORDER BY t.reminder_time ASC LIMIT 1",
        nativeQuery = true
    )
    TaskReminder findNextActiveTask (@Param("userId") String userId);

}
