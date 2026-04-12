package com.example.UberApp.service;

import com.example.UberApp.model.User;
import com.example.UberApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverAvailabilityService {

    private final UserRepository userRepository;

    // 🟢 Toggle driver online / offline
    public User toggleAvailability(Long driverId) {

        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("DRIVER NOT FOUND"));

        // flip available flag
        driver.setAvailable(!driver.isAvailable());

        return userRepository.save(driver);
    }

    // 🔍 Check if driver is available
    public boolean isAvailable(Long driverId) {

        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("DRIVER NOT FOUND"));

        return driver.isAvailable();
    }
}