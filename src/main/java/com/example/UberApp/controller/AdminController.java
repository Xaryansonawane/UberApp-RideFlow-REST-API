package com.example.UberApp.controller;

import com.example.UberApp.DTOs.ApiResponse;
import com.example.UberApp.model.enums.Role;
import com.example.UberApp.service.AdminService;
import com.example.UberApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final UserRepository userRepository;

    @GetMapping("/users")
    public ApiResponse<?> users() {
        return ApiResponse.success("ALL USERS", adminService.getAllUsers());
    }

    @GetMapping("/drivers")
    public ApiResponse<?> drivers() {
        return ApiResponse.success("ALL DRIVERS", adminService.getAllDrivers());
    }

    @GetMapping("/blocked-users")
    public ApiResponse<?> blockedUsers() {
        return ApiResponse.success("BLOCKED USERS",
                userRepository.findByRoleAndBlockedUntilAfter(Role.USER, LocalDateTime.now()));
    }

    @GetMapping("/blocked-drivers")
    public ApiResponse<?> blockedDrivers() {
        return ApiResponse.success("BLOCKED DRIVERS",
                userRepository.findByRoleAndBlockedUntilAfter(Role.DRIVER, LocalDateTime.now()));
    }
}