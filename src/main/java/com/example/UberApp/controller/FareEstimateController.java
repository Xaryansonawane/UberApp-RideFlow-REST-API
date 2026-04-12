package com.example.UberApp.controller;

import com.example.UberApp.service.FareService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fare")
@RequiredArgsConstructor
public class FareEstimateController {

    private final FareService fareService;

    // 💰 Ride start hone se pehle fare calculate
    @GetMapping("/estimate")
    public double estimateFare(
            @RequestParam double distance
    ) {
        // distance ke basis pe fare calculate hoga
        return fareService.calculateFare(distance);
    }
}