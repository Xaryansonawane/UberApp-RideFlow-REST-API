package com.example.UberApp.controller;

import com.example.UberApp.model.Ride;
import com.example.UberApp.service.RideHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rides")
@RequiredArgsConstructor
public class RideHistoryController {

    private final RideHistoryService rideHistoryService;

    // 📋 All rides for rider
    @GetMapping("/rider/{riderId}")
    public List<Ride> getRiderHistory(@PathVariable Long riderId) {
        return rideHistoryService.getRiderHistory(riderId);
    }

    // 🚗 All rides for driver
    @GetMapping("/driver/{driverId}")
    public List<Ride> getDriverHistory(@PathVariable Long driverId) {
        return rideHistoryService.getDriverHistory(driverId);
    }

    // ✅ Completed rides for rider
    @GetMapping("/rider/{riderId}/completed")
    public List<Ride> getCompletedRides(@PathVariable Long riderId) {
        return rideHistoryService.getCompletedRides(riderId);
    }

    // ❌ Cancelled rides for rider
    @GetMapping("/rider/{riderId}/cancelled")
    public List<Ride> getCancelledRides(@PathVariable Long riderId) {
        return rideHistoryService.getCancelledRides(riderId);
    }
}