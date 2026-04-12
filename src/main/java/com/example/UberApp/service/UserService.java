package com.example.UberApp.service;

import com.example.UberApp.model.User;
import com.example.UberApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 👤 User fetch
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("USER NOT FOUND"));
    }

    // ✏️ Update user
    public User updateUser(User user) {
        return userRepository.save(user);
    }
}