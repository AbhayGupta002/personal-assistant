package com.personal.assistant.controller;
import com.personal.assistant.dto.TaskDto;
import com.personal.assistant.entity.TaskDetails;
import com.personal.assistant.entity.TaskReminder;
import com.personal.assistant.entity.UserDetails;
import com.personal.assistant.enums.Status;
import com.personal.assistant.repository.TaskDetailsRepository;
import com.personal.assistant.repository.TaskReminderRepository;
import com.personal.assistant.repository.UserDetailsRepository;

import java.util.Optional;

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
        TaskReminder taskReminder = reminderRepo.findNextActiveTask(user.getId());
        TaskDetails taskDetails = Optional.of(taskRepo.findByTaskAndUserId(taskReminder.getTaskId(), user.getId())).orElseThrow(() -> new RuntimeException("Task not found for this id :: " + taskReminder.getTaskId()));

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
    
     @GetMapping(value = "get")
    public TaskDto getTask(@RequestBody TaskDto taskDto){
        TaskDetails taskDetails = taskRepo.findByTaskAndUserId(taskDto.getTaskId(), taskDto.getUserId())
                .orElseThrow(() -> new RuntimeException("task not existing"+taskDto.getTaskId()));
        TaskReminder taskReminder = reminderRepo.findByTaskId(taskDto.getTaskId())
                .orElseThrow(() -> new RuntimeException("task not existing"+taskDto.getTaskId()));
        TaskDto task = new TaskDto();
        task.setTaskId(taskDetails.getTaskId());
        task.setUserId(taskDetails.getUserId());
        task.setTaskName(taskDetails.getTaskName());
        task.setDateTime(taskDetails.getDateTime());
        task.setCreatedAt(taskDetails.getCreatedAt());
        task.setTaskCompleted(taskDetails.isTaskCompleted());
        task.setTaskStatus(Status.valueOf(taskDetails.getTaskStatus()));
        taskReminder.setReminderTime(taskReminder.getReminderTime());
        return task;

    }

    @PutMapping(value = "update")
    public TaskDto updateTask(@RequestBody TaskDto taskDto) {
        TaskDetails taskDetails = taskRepo.findByTaskAndUserId(taskDto.getTaskId(), taskDto.getUserId())
                .orElseThrow(() -> new RuntimeException("task not existing"+taskDto.getTaskId()));
        TaskReminder taskReminder = reminderRepo.findByTaskId(taskDto.getTaskId())
                .orElseThrow(() -> new RuntimeException("task not existing"+taskDto.getTaskId()));
        taskDetails.setTaskName(taskDto.getTaskName());
        taskDetails.setDateTime(taskDto.getDateTime());
        taskDetails.setTaskStatus(Status.ACTIVE.name());
        taskDetails.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        taskDetails.setTaskCompleted(false);
        TaskDetails savedTask = taskRepo.save(taskDetails);

        taskReminder.setReminderTime(taskDto.getReminderTime());
        taskReminder.setTaskId(savedTask.getTaskId());
        TaskReminder savedReminder = reminderRepo.save(taskReminder);

        taskDto.setTaskId(savedTask.getTaskId());
        taskDto.setTaskStatus(Status.valueOf(savedTask.getTaskStatus()));
        taskDto.setCreatedAt(savedTask.getCreatedAt());
        taskDto.setTaskCompleted(savedTask.isTaskCompleted());
        taskDto.setReminderTime(savedReminder.getReminderTime());

        return taskDto;

}

    
}
