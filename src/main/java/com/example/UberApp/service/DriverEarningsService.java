package com.example.UberApp.service;

import com.example.UberApp.model.Payment;
import com.example.UberApp.model.enums.PaymentStatus;
import com.example.UberApp.model.enums.RideStatus;
import com.example.UberApp.repository.PaymentRepository;
import com.example.UberApp.repository.RideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverEarningsService {

    private final PaymentRepository paymentRepository;
    private final RideRepository rideRepository;

    // 💰 Total earnings for driver
    public double getTotalEarnings(Long driverId) {
        Double total = paymentRepository.getTotalEarnings(driverId);
        return total != null ? total : 0.0;
    }

    // 📋 All completed payments for driver
    public List<Payment> getEarningsHistory(Long driverId) {
        return paymentRepository.findByDriverIdAndStatus(driverId, PaymentStatus.SUCCESS);
    }

    // ✅ Total completed rides
    public int getTotalCompletedRides(Long driverId) {
        return rideRepository.findAllByDriverIdAndStatus(driverId, RideStatus.COMPLETED).size();
    }

    // ❌ Total cancelled rides
    public int getTotalCancelledRides(Long driverId) {
        return rideRepository.findAllByDriverIdAndStatus(driverId, RideStatus.CANCELLED).size();
    }
}