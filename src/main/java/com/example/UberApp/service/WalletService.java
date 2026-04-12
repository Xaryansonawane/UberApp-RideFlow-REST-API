package com.example.UberApp.service;

import com.example.UberApp.model.User;
import com.example.UberApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final UserRepository userRepository;

    // 💰 Top-up wallet
    public User topUp(Long userId, double amount) {

        if (amount <= 0) {
            throw new RuntimeException("Amount must be greater than 0");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setWalletBalance(user.getWalletBalance() + amount);

        return userRepository.save(user);
    }

    // 💸 Deduct from wallet
    public User deduct(Long userId, double amount) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("USER NOT FOUND"));

        if (user.getWalletBalance() < amount) {
            throw new RuntimeException("INSUFFICIENT WALLET BALANCE");
        }

        user.setWalletBalance(user.getWalletBalance() - amount);

        return userRepository.save(user);
    }

    // 👀 Check wallet balance
    public double getBalance(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("USER NOT FOUND"));

        return user.getWalletBalance();
    }
}