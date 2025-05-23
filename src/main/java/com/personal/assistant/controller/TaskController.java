package com.personal.assistant.controller;

import com.personal.assistant.dto.TaskDto;
import com.personal.assistant.entity.TaskReminder;
import com.personal.assistant.enums.Status;
import com.personal.assistant.repository.TaskReminderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.personal.assistant.entity.TaskDetails;
import com.personal.assistant.entity.UserDetails;
import com.personal.assistant.repository.TaskDetailsRepository;
import com.personal.assistant.repository.UserDetailsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("tasks")
@Slf4j
public class TaskController {

    @Autowired
    private TaskDetailsRepository taskRepo;

    @Autowired
    private TaskReminderRepository reminderRepo;

    @Autowired
    private UserDetailsRepository userRepo;
    
    @PostMapping
    public TaskDto createTask(@RequestBody TaskDto taskDto) {
        UserDetails user = userRepo.findById(taskDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found for this id :: " + taskDto.getUserId()));
        String taskId = UUID.randomUUID().toString();

        TaskDetails taskDetails = new TaskDetails();
        taskDetails.setTaskId(taskId);
        taskDetails.setUserId(user.getId());
        taskDetails.setTaskName(taskDto.getTaskName());
        taskDetails.setDateTime(taskDto.getDateTime());
        taskDetails.setTaskStatus(Status.ACTIVE.name());
        taskDetails.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        taskDetails.setTaskCompleted(false);
        TaskDetails savedTask = taskRepo.save(taskDetails);

        TaskReminder taskReminder = new TaskReminder();
        taskReminder.setUserId(user.getId());
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

    @GetMapping
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

    @PutMapping
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

    @DeleteMapping
    public Status deleteTask(@RequestBody TaskDto taskDto) {
        TaskDetails taskDetails = taskRepo.findByTaskAndUserId(taskDto.getTaskId(), taskDto.getUserId())
                .orElseThrow(() -> new RuntimeException("task not existing"+taskDto.getTaskId()));
        TaskReminder taskReminder = reminderRepo.findByTaskId(taskDto.getTaskId())
                .orElseThrow(() -> new RuntimeException("task not existing"+taskDto.getTaskId()));

        taskDto.setTaskId(taskDetails.getTaskId());
        taskDetails.setTaskStatus(Status.INACTIVE.name());
        TaskDetails savedTask = taskRepo.save(taskDetails);

        return  taskDto.getTaskStatus();
    }
}
