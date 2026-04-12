package com.example.UberApp.DTOs;

import lombok.*;

public class UserDTO {

    @Getter
    @Setter
    public static class UserResponse {
        private Long id;
        private String name;
        private String email;
        private String phone;
        private double walletBalance;
    }

    @Getter
    @Setter
    public static class UpdateUserRequest {
        private String name;
        private String phone;
    }
}