package com.personal.assistant.dto;

import com.personal.assistant.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private String taskId;
    private String userId;
    private String taskName;
    private String dateTime;
    private String createdAt;
    private Status taskStatus;
    private boolean taskCompleted;
    private String reminderTime;
}
