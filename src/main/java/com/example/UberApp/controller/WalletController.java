package com.example.UberApp.controller;

import com.example.UberApp.model.User;
import com.example.UberApp.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    // 💰 Top-up wallet
    @PostMapping("/topup/{userId}")
    public User topUp(@PathVariable Long userId, @RequestParam double amount) {
        return walletService.topUp(userId, amount);
    }

    // 💸 Deduct from wallet
    @PostMapping("/deduct/{userId}")
    public User deduct(@PathVariable Long userId, @RequestParam double amount) {
        return walletService.deduct(userId, amount);
    }

    // 👀 Get balance
    @GetMapping("/balance/{userId}")
    public double getBalance(@PathVariable Long userId) {
        return walletService.getBalance(userId);
    }
}