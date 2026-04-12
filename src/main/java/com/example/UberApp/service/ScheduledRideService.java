package com.example.UberApp.service;

import com.example.UberApp.model.ScheduledRide;
import com.example.UberApp.model.User;
import com.example.UberApp.repository.ScheduledRideRepository;
import com.example.UberApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduledRideService {

    private final ScheduledRideRepository scheduledRideRepository;
    private final UserRepository userRepository;

    // 🕐 Book a scheduled ride
    public ScheduledRide scheduleRide(Long riderId, String pickup,
                                      String drop, LocalDateTime scheduledTime) {

        // ❌ Must be at least 30 mins in future
        if (scheduledTime.isBefore(LocalDateTime.now().plusMinutes(30))) {
            throw new RuntimeException("SCHEDULED TIME MUST BE AT LEAST 30 MINUTES FROM NOW");
        }

        User rider = userRepository.findById(riderId)
                .orElseThrow(() -> new RuntimeException("USER NOT FOUND"));

        ScheduledRide ride = new ScheduledRide();
        ride.setRider(rider);
        ride.setPickupLocation(pickup);
        ride.setDropLocation(drop);
        ride.setScheduledTime(scheduledTime);
        ride.setStatus("PENDING");

        return scheduledRideRepository.save(ride);
    }

    // 📋 Get all scheduled rides for a rider
    public List<ScheduledRide> getRiderScheduledRides(Long riderId) {
        return scheduledRideRepository.findByRiderId(riderId);
    }

    // ✅ Confirm a scheduled ride — admin or system
    public ScheduledRide confirmRide(Long rideId) {

        ScheduledRide ride = scheduledRideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("SCHEDULED RIDE NOT FOUND"));

        ride.setStatus("CONFIRMED");
        return scheduledRideRepository.save(ride);
    }

    // ❌ Cancel a scheduled ride
    public ScheduledRide cancelScheduledRide(Long rideId) {

        ScheduledRide ride = scheduledRideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("SCHEDULED RIDE NOT FOUND"));

        if (ride.getStatus().equals("CANCELLED")) {
            throw new RuntimeException("RIDE ALREADY CANCELLED");
        }

        ride.setStatus("CANCELLED");
        return scheduledRideRepository.save(ride);
    }

    // 🟡 Get all pending scheduled rides
    public List<ScheduledRide> getPendingRides() {
        return scheduledRideRepository.findByStatus("PENDING");
    }
}