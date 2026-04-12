package com.example.UberApp.controller;

import com.example.UberApp.model.ScheduledRide;
import com.example.UberApp.service.ScheduledRideService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/scheduled-rides")
@RequiredArgsConstructor
public class ScheduledRideController {

    private final ScheduledRideService scheduledRideService;

    // 🕐 Book scheduled ride
    @PostMapping("/book")
    public ScheduledRide scheduleRide(@RequestParam Long riderId,
                                      @RequestParam String pickup,
                                      @RequestParam String drop,
                                      @RequestParam String scheduledTime) {
        return scheduledRideService.scheduleRide(
                riderId, pickup, drop,
                LocalDateTime.parse(scheduledTime)
        );
    }

    // 📋 Get rider's scheduled rides
    @GetMapping("/rider/{riderId}")
    public List<ScheduledRide> getRiderScheduledRides(@PathVariable Long riderId) {
        return scheduledRideService.getRiderScheduledRides(riderId);
    }

    // ✅ Confirm ride
    @PutMapping("/confirm/{rideId}")
    public ScheduledRide confirmRide(@PathVariable Long rideId) {
        return scheduledRideService.confirmRide(rideId);
    }

    // ❌ Cancel scheduled ride
    @PutMapping("/cancel/{rideId}")
    public ScheduledRide cancelScheduledRide(@PathVariable Long rideId) {
        return scheduledRideService.cancelScheduledRide(rideId);
    }

    // 🟡 All pending rides — admin
    @GetMapping("/pending")
    public List<ScheduledRide> getPendingRides() {
        return scheduledRideService.getPendingRides();
    }
}