package com.example.UberApp.auth;

import com.example.UberApp.DTOs.AuthDTO;
import com.example.UberApp.model.User;
import com.example.UberApp.repository.UserRepository;
import com.example.UberApp.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // 🔐 REGISTER
    public AuthDTO.AuthResponse register(AuthDTO.RegisterRequest request) {

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setRole(com.example.UberApp.model.enums.Role.USER);

        userRepository.save(user);

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return new AuthDTO.AuthResponse(token, "REGISTER SUCCESS");
    }

    // 🔐 LOGIN
    public AuthDTO.AuthResponse login(AuthDTO.LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("USER NOT FOUND"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("INVALID PASSWORD");
        }

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return new AuthDTO.AuthResponse(token, "LOGIN SUCCESS");
    }
}