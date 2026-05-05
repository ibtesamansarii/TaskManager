package com.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

public class LoginRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    private String Email;

    @NotBlank(message = "Password is required")
    private String password;

    public LoginRequest() {
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
