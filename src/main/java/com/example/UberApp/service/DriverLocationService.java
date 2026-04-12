package com.example.UberApp.service;

import com.example.UberApp.model.DriverLocation;
import com.example.UberApp.repository.DriverLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverLocationService {

    private final DriverLocationRepository locationRepository;

    // 📍 Save or update location
    public DriverLocation updateLocation(Long driverId, double lat, double lng) {

        DriverLocation location = locationRepository.findByDriverId(driverId)
                .orElse(new DriverLocation());

        location.setDriverId(driverId);
        location.setLatitude(lat);
        location.setLongitude(lng);

        return locationRepository.save(location);
    }
}