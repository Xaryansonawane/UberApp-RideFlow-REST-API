package com.example.UberApp.service;

import org.springframework.stereotype.Service;

@Service
public class FareService {

    // 💰 Fare calculation
    public double calculateFare(double distance) {

        double baseFare = 50;
        double perKm = 10;

        return baseFare + (distance * perKm);
    }
}