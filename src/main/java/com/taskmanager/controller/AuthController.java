package com.taskmanager.controller;

import com.taskmanager.dto.LoginRequest;
import com.taskmanager.dto.LoginResponse;
import com.taskmanager.entity.User;
import com.taskmanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Optional<User> user = userService.loginUser(request.getEmail(), request.getPassword());

        if(user.isEmpty()) {
            return ResponseEntity.status(401).body(
                    new LoginResponse("Invalid email or password", null, null)
            );
        }

        User loggedInUser = user.get();

        LoginResponse response = new LoginResponse(
                "Login successful",
                loggedInUser.getEmail(),
                loggedInUser.getPassword()
        );

        return ResponseEntity.ok(response);
    }
}
