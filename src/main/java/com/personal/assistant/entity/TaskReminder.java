package com.personal.assistant.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task_reminder")
public class TaskReminder {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", insertable=false, updatable=false)
    private String id;

    @Column(name = "task_id", insertable=false, updatable=false)
    private String taskId;

    @OneToOne
    @JoinColumn(name = "task_id")
    private TaskDetails taskDetails;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "reminder_time" , nullable = false)
    private String reminderTime;
    
   
}
