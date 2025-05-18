package com.personal.assistant.controller;

import com.personal.assistant.dto.TaskDto;
import com.personal.assistant.entity.TaskReminder;
import com.personal.assistant.enums.Status;
import com.personal.assistant.repository.TaskReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody; 
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.personal.assistant.entity.TaskDetails;
import com.personal.assistant.entity.UserDetails;
import com.personal.assistant.repository.TaskDetailsRepository;
import com.personal.assistant.repository.UserDetailsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("tasks") 
public class TaskController {

    @Autowired
    private TaskDetailsRepository taskRepo;

    @Autowired
    private TaskReminderRepository reminderRepo;

    @Autowired
    private UserDetailsRepository userRepo;
    
    @PostMapping(value = "create")
    public TaskDto createTask(@RequestBody TaskDto taskDto) {
        UserDetails user = userRepo.findById(taskDto.getUserId()).orElseThrow(() -> new RuntimeException("User not found for this id :: " + taskDto.getUserId()));
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
        taskReminder.setTaskId(savedTask.getTaskId());
        taskReminder.setReminderTime(taskDto.getReminderTime());
        TaskReminder reminderResponse = reminderRepo.save(taskReminder);

        taskDto.setTaskId(savedTask.getTaskId());
        taskDto.setTaskStatus(Status.valueOf(savedTask.getTaskStatus()));
        taskDto.setCreatedAt(savedTask.getCreatedAt());
        taskDto.setTaskCompleted(savedTask.isTaskCompleted());

        return taskDto;
    }

    @GetMapping(value = "get")
    public TaskDto getTask(@RequestBody TaskDto taskDto){
        TaskDetails taskDetails = taskRepo.findById(taskDto.getTaskId()).orElseThrow(() -> new RuntimeException("task not existing"+taskDto.getTaskId()));
        TaskReminder taskReminder = new TaskReminder();
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
    public ResponseEntity<TaskDetails> updateTask(@RequestBody TaskDetails updatetask) {
    TaskDetails existingTask = taskRepo.findById(updatetask.getTaskId()).orElseThrow(() -> new RuntimeException("Task not found for this id :: " + updatetask.getTaskId()));
    existingTask.setTaskName(updatetask.getTaskName());
    existingTask.setDateTime(updatetask.getDateTime());
    existingTask.setTaskStatus(updatetask.getTaskStatus());
    existingTask.setTaskCompleted(updatetask.isTaskCompleted());

    TaskDetails updatedTask = taskRepo.save(existingTask);

    return ResponseEntity.ok(updatedTask);
}


    @PostMapping(value = "delete")
    public ResponseEntity<TaskDetails> deleteTask(@RequestBody String taskId, @RequestBody String userId){
        UserDetails user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found for this id :: " + userId));
        TaskDetails task = Optional.of(taskRepo.findByTaskAndUserId(taskId, user.getId())).orElseThrow(() -> new RuntimeException("Task not found for this id :: " + taskId));
        task.setTaskStatus(Status.INACTIVE.name());
        TaskDetails deletedTask = taskRepo.save(task);
        return ResponseEntity.ok(deletedTask); 
    }
}