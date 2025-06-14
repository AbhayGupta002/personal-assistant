package com.personal.assistant.controller;

import com.personal.assistant.dto.ErrorDetails;
import com.personal.assistant.dto.Response;
import com.personal.assistant.dto.TaskDto;
import com.personal.assistant.entity.TaskReminder;
import com.personal.assistant.enums.Status;
import com.personal.assistant.repository.TaskReminderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
@Slf4j
public class TaskController {

    @Autowired
    private TaskDetailsRepository taskRepo;

    @Autowired
    private TaskReminderRepository reminderRepo;

    @Autowired
    private UserDetailsRepository userRepo;
    
    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody TaskDto taskDto) {
        Response response = new Response();

        Optional<UserDetails> userOptional = userRepo.findById(taskDto.getUserId());
        if (userOptional.isEmpty()) {
            ErrorDetails errorDetails = new ErrorDetails(HttpStatus.NOT_FOUND, "User not found for id: " + taskDto.getUserId());
            response.setError(errorDetails);
            return ResponseEntity.ofNullable(response);
        }
        UserDetails user = userOptional.get();
        String taskId = UUID.randomUUID().toString();
        TaskDetails savedTask = new TaskDetails();
        TaskDetails taskDetails = new TaskDetails();
        taskDetails.setTaskId(taskId);
        taskDetails.setUserId(user.getId());
        taskDetails.setTaskName(taskDto.getTaskName());
        taskDetails.setDateTime(taskDto.getDateTime());
        taskDetails.setTaskStatus(Status.ACTIVE.name());
        taskDetails.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        taskDetails.setTaskCompleted(false);
        try {
            savedTask = taskRepo.save(taskDetails);
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Task could not be created. Please try again.");
            response.setError(errorDetails);
            return ResponseEntity.ofNullable(response);
        }
        TaskReminder savedReminder = new TaskReminder();
        TaskReminder taskReminder = new TaskReminder();
        taskReminder.setUserId(user.getId());
        taskReminder.setReminderTime(taskDto.getReminderTime());
        taskReminder.setTaskId(savedTask.getTaskId());
        try {
            savedReminder = reminderRepo.save(taskReminder);;
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Task could not be created. Please try again.");
            response.setError(errorDetails);
            return ResponseEntity.ofNullable(response);
        }

        taskDto.setTaskId(savedTask.getTaskId());
        taskDto.setTaskStatus(Status.valueOf(savedTask.getTaskStatus()));
        taskDto.setCreatedAt(savedTask.getCreatedAt());
        taskDto.setTaskCompleted(savedTask.isTaskCompleted());
        taskDto.setReminderTime(savedReminder.getReminderTime());

        response.setData(taskDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public TaskDto getTask(@RequestBody TaskDto taskDto){
        TaskDto task = new TaskDto();
        try{
            TaskDetails taskDetails = taskRepo.findByTaskAndUserId(taskDto.getTaskId(), taskDto.getUserId())
                    .orElseThrow(() -> new RuntimeException("Task not found"));
            TaskReminder taskReminder = reminderRepo.findByTaskId(taskDto.getTaskId())
                    .orElseThrow(() -> new RuntimeException("task not existing"+taskDto.getTaskId()));
            task.setTaskId(taskDetails.getTaskId());
            task.setUserId(taskDetails.getUserId());
            task.setTaskName(taskDetails.getTaskName());
            task.setDateTime(taskDetails.getDateTime());
            task.setCreatedAt(taskDetails.getCreatedAt());
            task.setTaskCompleted(taskDetails.isTaskCompleted());
            task.setTaskStatus(Status.valueOf(taskDetails.getTaskStatus()));
            taskReminder.setReminderTime(taskReminder.getReminderTime());
        } catch (Exception e) {
            ErrorDetails errorDetails =new ErrorDetails(HttpStatus.NOT_FOUND,
                    "Invalid Task Or UserId"+taskDto.getTaskId()+taskDto.getUserId());
        }
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

        TaskDetails savedTask = new TaskDetails();
        try{
            savedTask = taskRepo.save(taskDetails);
        }catch (Exception e){
            ErrorDetails errorDetails = new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Task Not Updated");
        }

        taskReminder.setReminderTime(taskDto.getReminderTime());
        taskReminder.setTaskId(savedTask.getTaskId());
        TaskReminder savedReminder;
        try{
            savedReminder = reminderRepo.save(taskReminder);
            taskDto.setTaskId(savedTask.getTaskId());
            taskDto.setTaskStatus(Status.valueOf(savedTask.getTaskStatus()));
            taskDto.setCreatedAt(savedTask.getCreatedAt());
            taskDto.setTaskCompleted(savedTask.isTaskCompleted());
            taskDto.setReminderTime(savedReminder.getReminderTime());
        }catch (Exception e){
            ErrorDetails errorDetails = new ErrorDetails(HttpStatus.NOT_IMPLEMENTED,
                    "task not saved");
        }

        return taskDto;

}

    @DeleteMapping
    public Status deleteTask(@RequestBody TaskDto taskDto) {

        TaskDetails taskDetails = taskRepo.findByTaskAndUserId(taskDto.getTaskId(), taskDto.getUserId())
                .orElseThrow(() -> new RuntimeException("task not existing"+taskDto.getTaskId()));
//        TaskReminder taskReminder = reminderRepo.findByTaskId(taskDto.getTaskId())
//                .orElseThrow(() -> new RuntimeException("task not existing"+taskDto.getTaskId()));

        taskDto.setTaskId(taskDetails.getTaskId());
        taskDetails.setTaskStatus(Status.INACTIVE.name());
        TaskDetails savedTask = taskRepo.save(taskDetails);

        return  taskDto.getTaskStatus();
    }
}
