package com.personal.assistant.repository;

import com.personal.assistant.entity.TaskDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskDetailsRepository extends JpaRepository<TaskDetails, String> {

    @Query(
        value = "SELECT * FROM task_details t WHERE t.user_id = :userId AND t.task_id = :taskId ORDER BY t.time ASC LIMIT 1",
        nativeQuery = true
    )
    TaskDetails findByTaskAndUserId(@Param("taskId") String taskId, @Param("userId") String userId);

}