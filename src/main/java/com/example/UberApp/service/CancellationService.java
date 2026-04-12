package com.example.UberApp.service;

import com.example.UberApp.model.User;
import com.example.UberApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CancellationService {

    private final UserRepository userRepository;

    private static final int FREE_LIMIT = 3;
    private static final double PENALTY = 50.0;

    // 🚫 CHECK IF USER IS BLOCKED
    public boolean isBlocked(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("USER NOT FOUND"));

        if (user.getBlockedUntil() != null) {

            // ⛔ still blocked
            if (user.getBlockedUntil().isAfter(LocalDateTime.now())) {
                return true;
            }

            // 🔓 auto unblock
            user.setBlockedUntil(null);
            user.setCancellationCount(0);
            user.setActive(true);
            userRepository.save(user);
        }

        return false;
    }

    // ❌ USER CANCELLATION LOGIC
    public String addCancellation(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("USER NOT FOUND"));

        int count = user.getCancellationCount() + 1;
        user.setCancellationCount(count);

        // ✅ 1st & 2nd → normal
        if (count <= 2) {
            userRepository.save(user);
            return "CANCELLATION ADDED";
        }

        // ⚠️ 3rd → warning
        if (count == 3) {
            userRepository.save(user);
            return "WARNING: NEXT CANCELLATION WILL LEAD TO PENALTY AND 48HR BLOCK";
        }

        // 🚨 4th → penalty + block
        if (count == 4) {

            user.setWalletBalance(user.getWalletBalance() - PENALTY);
            user.setBlockedUntil(LocalDateTime.now().plusHours(48));
            user.setActive(false);

            userRepository.save(user);

            return "PENALTY APPLIED: ₹50 DEDUCTED AND USER BLOCKED FOR 48 HOURS";
        }

        userRepository.save(user);
        return "CANCELLATION UPDATED";
    }

    // 🚗 DRIVER CANCELLATION LOGIC
    public String driverCancellation(Long driverId) {

        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("DRIVER NOT FOUND"));

        int count = driver.getCancellationCount() + 1;
        driver.setCancellationCount(count);

        // ✅ 1st & 2nd → normal
        if (count <= 2) {
            userRepository.save(driver);
            return "DRIVER CANCELLATION ADDED";
        }

        // ⚠️ 3rd → warning
        if (count == 3) {
            userRepository.save(driver);
            return "DRIVER WARNING: NEXT CANCEL WILL APPLY PENALTY";
        }

        // 💸 4th → penalty ₹50
        if (count == 4) {
            driver.setWalletBalance(driver.getWalletBalance() - PENALTY);
            userRepository.save(driver);
            return "DRIVER PENALTY: ₹50 DEDUCTED";
        }

        // 🚨 5th → penalty + block
        if (count >= 5) {

            driver.setWalletBalance(driver.getWalletBalance() - PENALTY);
            driver.setActive(false);
            driver.setBlockedUntil(LocalDateTime.now().plusHours(24));

            userRepository.save(driver);

            return "DRIVER BLOCKED FOR 24 HOURS AND ₹50 DEDUCTED";
        }

        userRepository.save(driver);
        return "DRIVER UPDATED";
    }
}