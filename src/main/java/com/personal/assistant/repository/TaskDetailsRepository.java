package com.personal.assistant.repository;

import com.personal.assistant.dto.TaskDto;
import com.personal.assistant.entity.TaskDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaskDetailsRepository extends JpaRepository<TaskDetails, String> {

    @Query(
        value = "SELECT * FROM task_details WHERE user_id = :userId AND task_id = :taskId",
        nativeQuery = true
    )
    Optional<TaskDetails> findByTaskAndUserId(@Param("taskId") String taskId, @Param("userId") String userId);

}
