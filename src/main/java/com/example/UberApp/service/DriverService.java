package com.example.UberApp.service;

import com.example.UberApp.model.DriverLocation;
import com.example.UberApp.model.User;
import com.example.UberApp.repository.DriverLocationRepository;
import com.example.UberApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverLocationRepository locationRepository;
    private final UserRepository userRepository;

    // 📍 Driver location update
    public void updateLocation(Long driverId, double lat, double lng) {

        DriverLocation location = locationRepository
                .findByDriverId(driverId)
                .orElse(new DriverLocation());

        location.setDriverId(driverId);
        location.setLatitude(lat);
        location.setLongitude(lng);

        locationRepository.save(location);
    }

    // 🟢 Driver availability toggle
    public void updateAvailability(Long driverId, boolean status) {

        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("DRIVER NOT FOUND"));

        driver.setAvailable(status);
        userRepository.save(driver);
    }
}