package com.taskmanager.controller;

import com.taskmanager.dto.CreateTaskRequest;
import com.taskmanager.dto.TaskResponse;
import com.taskmanager.entity.Task;
import com.taskmanager.entity.TaskStatus;
import com.taskmanager.entity.User;
import com.taskmanager.service.TaskService;
import com.taskmanager.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<TaskResponse> createTask(@PathVariable Long userId, @RequestBody CreateTaskRequest request) {
        Optional<User> user = userService.getUserById(userId);

        if(user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Task task = mapToTask(request);
        task.setUser(user.get());
        Task savedTask = taskService.createTask(task);
        TaskResponse response = mapToTaskResponse(savedTask);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        List<TaskResponse> responses = new ArrayList<>();

        for(Task task : tasks) {
            responses.add(mapToTaskResponse(task));
        }
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        Optional<Task> task = taskService.getTaskById(id);

        if(task.isPresent()) {
            TaskResponse response = mapToTaskResponse(task.get());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }

//        return task.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskResponse>> getTasksByUser(@PathVariable Long userId) {
        Optional<User> user = userService.getUserById(userId);

        if(user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Task> tasks = taskService.getTasksByUser(user.get());
        List<TaskResponse> responses = new ArrayList<>();

        for(Task task : tasks) {
            responses.add(mapToTaskResponse(task));
        }

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<TaskResponse>> getTasksByUserAndStatus(@PathVariable Long userId, @PathVariable TaskStatus status) {
        Optional<User> user = userService.getUserById(userId);

        if(user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Task> tasks = taskService.getTasksByUserAndStatus(user.get(), status);
        List<TaskResponse> responses = new ArrayList<>();

        for(Task task : tasks) {
            responses.add(mapToTaskResponse(task));
        }

        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody CreateTaskRequest request) {
        Task updatedTask = mapToTask(request);
        Optional<Task> task = taskService.updateTask(id, updatedTask);

        if(task.isPresent()) {
            TaskResponse response = mapToTaskResponse(task.get());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }

//        return task.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status/{status}")
    public ResponseEntity<TaskResponse> updateTaskStatus(@PathVariable Long id, @PathVariable TaskStatus status) {
        Optional<Task> task = taskService.updateTaskStatus(id, status);

        if(task.isPresent()) {
            TaskResponse response = mapToTaskResponse(task.get());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }

//        return task.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        Optional<Task> task = taskService.getTaskById(id);

        if (task.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    private Task mapToTask(CreateTaskRequest taskRequest) {
        Task task = new Task();

        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setStatus(taskRequest.getStatus());
        task.setDueDate(taskRequest.getDueDate());

        return task;
    }

    private TaskResponse mapToTaskResponse(Task task) {

        TaskResponse response = new TaskResponse();

        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setDueDate(task.getDueDate());
        response.setUserId(task.getUser().getId());
        response.setCreatedAt(task.getCreatedAt());
        response.setUpdatedAt(task.getUpdatedAt());

        return response;
    }
}
