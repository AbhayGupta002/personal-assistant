package com.personal.assistant.controller;

import com.personal.assistant.dto.TaskDto;
import com.personal.assistant.entity.TaskDetails;
import com.personal.assistant.entity.TaskReminder;
import com.personal.assistant.entity.UserDetails;
import com.personal.assistant.enums.Status;
import com.personal.assistant.repository.TaskDetailsRepository;
import com.personal.assistant.repository.TaskReminderRepository;
import com.personal.assistant.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("reminder")
public class ReminderController {

    @Autowired
    private TaskDetailsRepository taskRepo;

    @Autowired
    private UserDetailsRepository userRepo;

    @Autowired
    private TaskReminderRepository reminderRepo;

    @PostMapping("/")
    public TaskDto getReminder(@RequestBody String userId) {
        UserDetails user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found for this id :: " + userId));
        TaskReminder taskReminder = reminderRepo.findNextActiveTask(user.getId())
                .orElseThrow(() -> new RuntimeException("Task Reminder not found for this id :: " + userId));
        TaskDetails taskDetails = taskRepo.findByTaskAndUserId(taskReminder.getTaskDetails().getTaskId(), user.getId())
                .orElseThrow(() -> new RuntimeException("Task not found for this id :: " + taskReminder.getTaskDetails().getTaskId()));

        TaskDto task = new TaskDto();
        task.setTaskId(taskDetails.getTaskId());
        task.setUserId(taskDetails.getUserId());
        task.setTaskName(taskDetails.getTaskName());
        task.setDateTime(taskDetails.getDateTime());
        task.setTaskStatus(Status.valueOf(taskDetails.getTaskStatus()));
        task.setCreatedAt(taskDetails.getCreatedAt());
        task.setTaskCompleted(taskDetails.isTaskCompleted());
        task.setReminderTime(taskReminder.getReminderTime());

        return task;
    }

}
