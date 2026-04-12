package com.example.UberApp.controller;

import com.example.UberApp.model.User;
import com.example.UberApp.service.NearestDriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class NearestDriverController {

    private final NearestDriverService nearestDriverService;

    // 📍 Find nearest driver
    @GetMapping("/nearest")
    public User findNearest(@RequestParam double lat, @RequestParam double lng) {
        return nearestDriverService.findNearest(lat, lng);
    }

    // 📍 Find all drivers within radius
    @GetMapping("/nearby")
    public List<User> findAllNearby(@RequestParam double lat,
                                    @RequestParam double lng,
                                    @RequestParam(defaultValue = "5.0") double radius) {
        return nearestDriverService.findAllNearby(lat, lng, radius);
    }
}