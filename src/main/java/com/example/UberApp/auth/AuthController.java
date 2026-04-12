package com.example.UberApp.auth;

import com.example.UberApp.DTOs.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody AuthDTO.LoginRequest request) {
        return ApiResponse.success("LOGIN SUCCESS", authService.login(request));
    }

    @PostMapping("/register")
    public ApiResponse<?> register(@RequestBody AuthDTO.RegisterRequest request) {
        return ApiResponse.success("REGISTER SUCCESS", authService.register(request));
    }
}