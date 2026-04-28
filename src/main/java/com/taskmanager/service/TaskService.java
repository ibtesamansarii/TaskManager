package com.taskmanager.service;

import com.taskmanager.entity.Task;
import com.taskmanager.entity.TaskStatus;
import com.taskmanager.entity.User;
import com.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public List<Task> getTasksByUser(User user) { return taskRepository.findByUser(user); }

    public List<Task> getTasksByUserAndStatus(User user, TaskStatus status) {
        return taskRepository.findByUserAndStatus(user, status);
    }

    public Optional<Task> updateTask(Long id, Task updatedTask) {

        Optional<Task> existingTaskOptional = taskRepository.findById(id);

        if(existingTaskOptional.isEmpty()) {
            return Optional.empty();
        }

        Task existingTask = existingTaskOptional.get();
        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setStatus(updatedTask.getStatus());
        existingTask.setDueDate(updatedTask.getDueDate());

        Task savedTask = taskRepository.save(existingTask);
        return Optional.of(savedTask);
    }

    public Optional<Task> updateTaskStatus(Long id, TaskStatus status) {

        Optional<Task> existingTaskOptional = taskRepository.findById(id);

        if(existingTaskOptional.isEmpty()) {
            return Optional.empty();
        }

        Task existingTask = existingTaskOptional.get();
        existingTask.setStatus(status);

        Task savedTask = taskRepository.save(existingTask);
        return Optional.of(savedTask);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
