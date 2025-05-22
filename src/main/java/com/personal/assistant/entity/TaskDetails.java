package com.personal.assistant.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task_details")
public class TaskDetails {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", insertable=false, updatable=false)
    private String id;

    @Column(name = "task_id", unique = true)
    private String taskId;

    @OneToOne(mappedBy = "taskDetails", cascade = CascadeType.ALL)
    private TaskReminder taskReminder;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "task_name", nullable = false, insertable = true, updatable = true)
    private String taskName;

    @Column(name = "date_time", insertable = true, updatable = true)
    private String dateTime;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "task_status", nullable = false)
    private String taskStatus;

    @Column(name = "task_completed")
    private boolean taskCompleted;

}
