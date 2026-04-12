package com.example.UberApp.controller;

import com.example.UberApp.model.User;
import com.example.UberApp.service.DriverAvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/driver")
@RequiredArgsConstructor
public class DriverAvailabilityController {

    private final DriverAvailabilityService driverAvailabilityService;

    // 🟢 Toggle online / offline
    @PutMapping("/toggle/{driverId}")
    public User toggleAvailability(@PathVariable Long driverId) {
        return driverAvailabilityService.toggleAvailability(driverId);
    }

    // 🔍 Check availability status
    @GetMapping("/status/{driverId}")
    public boolean getStatus(@PathVariable Long driverId) {
        return driverAvailabilityService.isAvailable(driverId);
    }
}