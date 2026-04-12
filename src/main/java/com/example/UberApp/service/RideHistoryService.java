package com.example.UberApp.service;

import com.example.UberApp.model.Ride;
import com.example.UberApp.model.enums.RideStatus;
import com.example.UberApp.repository.RideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RideHistoryService {

    private final RideRepository rideRepository;

    // 📋 All rides for a rider
    public List<Ride> getRiderHistory(Long riderId) {
        return rideRepository.findByRiderId(riderId);
    }

    // 🚗 All rides for a driver
    public List<Ride> getDriverHistory(Long driverId) {
        return rideRepository.findByDriverId(driverId);
    }

    // ✅ Only completed rides for a rider
    public List<Ride> getCompletedRides(Long riderId) {
        return rideRepository.findAllByRiderIdAndStatus(riderId, RideStatus.COMPLETED);
    }

    // ❌ Only cancelled rides for a rider
    public List<Ride> getCancelledRides(Long riderId) {
        return rideRepository.findAllByRiderIdAndStatus(riderId, RideStatus.CANCELLED);
    }
}