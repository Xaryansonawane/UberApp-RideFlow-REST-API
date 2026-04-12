package com.example.UberApp.controller;

import com.example.UberApp.model.Ride;
import com.example.UberApp.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ride")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;

    // 🚀 REQUEST RIDE
    @PostMapping("/request")
    public Ride requestRide(@RequestParam Long userId,
                            @RequestParam double lat,
                            @RequestParam double lng,
                            @RequestParam String pickup,
                            @RequestParam String drop,
                            @RequestParam double distanceKm) {

        return rideService.requestRide(userId, lat, lng, pickup, drop, distanceKm);
    }

    // 🚗 ACCEPT RIDE
    @PutMapping("/accept/{rideId}")
    public Ride acceptRide(@PathVariable Long rideId) {
        return rideService.acceptRide(rideId);
    }

    // ✅ COMPLETE RIDE
    @PutMapping("/complete/{rideId}")
    public Ride completeRide(@PathVariable Long rideId) {
        return rideService.completeRide(rideId);
    }

    // ❌ CANCEL RIDE
    @PutMapping("/cancel/{rideId}")
    public Map<String, Object> cancelRide(@PathVariable Long rideId) {
        return rideService.cancelRide(rideId);
    }
}