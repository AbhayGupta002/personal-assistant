package com.personal.assistant.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task_details")
public class TaskDetails {
    @Id
    @Column(name = "task_id")
    private String taskId;

    @OneToOne(mappedBy = "taskDetails", cascade = CascadeType.ALL)
    private TaskReminder taskReminder;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "task_name", nullable = false)
    private String taskName;

    @Column(name = "date_time")
    private String dateTime;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "task_status", nullable = false)
    private String taskStatus;

    @Column(name = "task_completed")
    private boolean taskCompleted;

}
