package com.taskmanager.controller;

import com.taskmanager.dto.CreateUserRequest;
import com.taskmanager.dto.UserResponse;
import com.taskmanager.entity.User;
import com.taskmanager.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request) {
        User user = mapToUser(request);
        User savedUser = userService.createUser(user);
        UserResponse response = mapToUserResponse(savedUser);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        List<UserResponse> users = new ArrayList<>();

        for (User user : userList) {
            users.add(mapToUserResponse(user));
        }

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);

        if (user.isPresent()) {
            UserResponse response = mapToUserResponse(user.get());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }

//        if (user.isPresent()) {
//            return ResponseEntity.ok(user.get());
//        } else {
//            return ResponseEntity.notFound().build();
//        }

//        return user.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody CreateUserRequest request) {
        User updatedUser = mapToUser(request);
        Optional<User> user = userService.updateUser(id, updatedUser);

        if(user.isPresent()) {
            UserResponse response = mapToUserResponse(user.get());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);

        if(user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    private User mapToUser(CreateUserRequest request) {
        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setDateOfJoining(request.getDateOfJoining());
        user.setAvatarUrl(request.getAvatarUrl());

        return user;
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setDateOfBirth(user.getDateOfBirth());
        response.setDateOfJoining(user.getDateOfJoining());
        response.setAvatarUrl(user.getAvatarUrl());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());

        return response;
    }
}
