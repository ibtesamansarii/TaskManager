package com.taskmanager.controller;

import com.taskmanager.entity.Task;
import com.taskmanager.entity.TaskStatus;
import com.taskmanager.entity.User;
import com.taskmanager.service.TaskService;
import com.taskmanager.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Task> createTask(@PathVariable Long userId, @RequestBody Task task) {
        Optional<User> user = userService.getUserById(userId);

        if(user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        task.setUser(user.get());
        Task savedTask = taskService.createTask(task);
        return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> task = taskService.getAllTasks();
        return ResponseEntity.ok(task);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Optional<Task> task = taskService.getTaskById(id);
        return task.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> getTasksByUser(@PathVariable Long userId) {
        Optional<User> user = userService.getUserById(userId);

        if(user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Task> tasks = taskService.getTasksByUser(user.get());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/user/{userId}/status/{statusId}")
    public ResponseEntity<List<Task>> getTasksByUser(@PathVariable Long userId, @PathVariable TaskStatus status) {
        Optional<User> user = userService.getUserById(userId);

        if(user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Task> tasks = taskService.getTasksByUserAndStatus(user.get(), status);
        return ResponseEntity.ok(tasks);
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
}
