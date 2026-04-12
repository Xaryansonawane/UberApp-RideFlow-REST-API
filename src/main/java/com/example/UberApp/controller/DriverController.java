package com.example.UberApp.controller;

import com.example.UberApp.model.DriverLocation;
import com.example.UberApp.service.DriverLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/driver")
@RequiredArgsConstructor
public class DriverController {

    private final DriverLocationService locationService;

    // 📍 Update location
    @PostMapping("/location")
    public DriverLocation updateLocation(
            @RequestParam Long driverId,
            @RequestParam double lat,
            @RequestParam double lng
    ) {
        return locationService.updateLocation(driverId, lat, lng);
    }
}