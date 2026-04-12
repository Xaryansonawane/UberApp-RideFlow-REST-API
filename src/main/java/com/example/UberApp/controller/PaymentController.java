package com.example.UberApp.controller;

import com.example.UberApp.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // 💳 Ride ke baad payment process
    @PostMapping("/pay")
    public String processPayment(
            @RequestParam Long rideId,
            @RequestParam String method
    ) {
        // method = WALLET / CASH / UPI
        return paymentService.processPayment(rideId, method);
    }

    // 💰 Wallet balance check
    @GetMapping("/wallet/{userId}")
    public double getWallet(@PathVariable Long userId) {
        return paymentService.getWalletBalance(userId);
    }
}