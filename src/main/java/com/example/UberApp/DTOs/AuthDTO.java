package com.example.UberApp.DTOs;

import lombok.*;

public class AuthDTO {

    @Getter
    @Setter
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @Getter
    @Setter
    public static class RegisterRequest {
        private String name;
        private String email;
        private String password;
        private String phone;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class AuthResponse {
        private String token;
        private String message;
    }
}