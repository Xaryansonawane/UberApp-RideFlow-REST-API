package com.example.UberApp.service;

import com.example.UberApp.model.Ride;
import com.example.UberApp.model.User;
import com.example.UberApp.model.enums.RideStatus;
import com.example.UberApp.model.enums.Role;
import com.example.UberApp.repository.PaymentRepository;
import com.example.UberApp.repository.RideRepository;
import com.example.UberApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final RideRepository rideRepository;
    private final PaymentRepository paymentRepository;

    // 👥 Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 🚗 Get all drivers
    public List<User> getAllDrivers() {
        return userRepository.findByRole(Role.DRIVER);
    }

    // 🚀 Get all rides
    public List<Ride> getAllRides() {
        return rideRepository.findAll();
    }

    // ✅ Get all completed rides
    public List<Ride> getCompletedRides() {
        return rideRepository.findByStatus(RideStatus.COMPLETED);
    }

    // ❌ Get all cancelled rides
    public List<Ride> getCancelledRides() {
        return rideRepository.findByStatus(RideStatus.CANCELLED);
    }

    // 💰 Total revenue
    public double getTotalRevenue() {
        return paymentRepository.findAll()
                .stream()
                .mapToDouble(p -> p.getAmount())
                .sum();
    }

    // 🚫 Block user
    public User blockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("USER NOT FOUND"));
        user.setCancellationCount(99);
        return userRepository.save(user);
    }

    // ✅ Unblock user
    public User unblockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("USER NOT FOUND"));
        user.setCancellationCount(0);
        return userRepository.save(user);
    }
}