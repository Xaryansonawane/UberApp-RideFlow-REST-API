package com.example.UberApp.controller;

import com.example.UberApp.model.Payment;
import com.example.UberApp.service.DriverEarningsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/driver/earnings")
@RequiredArgsConstructor
public class DriverEarningsController {

    private final DriverEarningsService driverEarningsService;

    // 💰 Total earnings
    @GetMapping("/total/{driverId}")
    public double getTotalEarnings(@PathVariable Long driverId) {
        return driverEarningsService.getTotalEarnings(driverId);
    }

    // 📋 Earnings history
    @GetMapping("/history/{driverId}")
    public List<Payment> getEarningsHistory(@PathVariable Long driverId) {
        return driverEarningsService.getEarningsHistory(driverId);
    }

    // ✅ Completed rides count
    @GetMapping("/completed/{driverId}")
    public int getCompletedRides(@PathVariable Long driverId) {
        return driverEarningsService.getTotalCompletedRides(driverId);
    }

    // ❌ Cancelled rides count
    @GetMapping("/cancelled/{driverId}")
    public int getCancelledRides(@PathVariable Long driverId) {
        return driverEarningsService.getTotalCancelledRides(driverId);
    }
}