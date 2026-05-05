package com.taskmanager.dto;

public class LoginResponse {

    private String message;
    private String userId;
    private String email;

    public LoginResponse() {
    }

    public LoginResponse(String message, String userId, String email) {
        this.message = message;
        this.userId = userId;
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
