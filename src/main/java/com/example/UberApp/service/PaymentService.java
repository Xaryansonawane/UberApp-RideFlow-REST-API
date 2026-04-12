package com.example.UberApp.service;

import com.example.UberApp.model.Payment;
import com.example.UberApp.model.Ride;
import com.example.UberApp.model.User;
import com.example.UberApp.model.enums.PaymentStatus;
import com.example.UberApp.repository.PaymentRepository;
import com.example.UberApp.repository.RideRepository;
import com.example.UberApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RideRepository rideRepository;
    private final UserRepository userRepository;

    // 💳 Payment process
    public String processPayment(Long rideId, String method) {

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("RIDE NOT FOUND"));

        User user = ride.getRider();

        double fare = 100; // ⚠️ baad me dynamic karenge

        Payment payment = new Payment();
        payment.setRide(ride);
        payment.setAmount(fare);
        payment.setMethod(method);
        payment.setStatus(PaymentStatus.PENDING);

        // 💰 Wallet payment
        if ("WALLET".equalsIgnoreCase(method)) {

            if (user.getWalletBalance() < fare) {
                payment.setStatus(PaymentStatus.FAILED);
                paymentRepository.save(payment);
                throw new RuntimeException("INSUFFICIENT BALANCE");
            }

            user.setWalletBalance(user.getWalletBalance() - fare);
            userRepository.save(user);

            payment.setStatus(PaymentStatus.SUCCESS);
        } else {
            // 💵 CASH / UPI assume success
            payment.setStatus(PaymentStatus.SUCCESS);
        }

        paymentRepository.save(payment);

        return "PAYMENT SUCCESSFULL VIA " + method;
    }

    // 💰 Wallet check
    public double getWalletBalance(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("USER NOT FOUND"))
                .getWalletBalance();
    }

    // ❌ Cancel payment (ride cancel ke case me)
    public void cancelPayment(Long rideId, String cancelledBy) {

        Payment payment = paymentRepository.findByRideId(rideId);

        if (payment != null) {
            payment.setStatus(PaymentStatus.CANCELLED);
            payment.setCancelledBy(cancelledBy);
            paymentRepository.save(payment);
        }
    }
}