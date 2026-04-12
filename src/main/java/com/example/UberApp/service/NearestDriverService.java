package com.example.UberApp.service;

import com.example.UberApp.model.DriverLocation;
import com.example.UberApp.model.User;
import com.example.UberApp.repository.DriverLocationRepository;
import com.example.UberApp.repository.UserRepository;
import com.example.UberApp.utility.DistanceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NearestDriverService {

    private final DriverLocationRepository locationRepository;
    private final UserRepository userRepository;

    // 📍 Find nearest single driver
    public User findNearest(double lat, double lng) {

        List<DriverLocation> drivers = locationRepository.findAll();

        DriverLocation nearest = drivers.stream()
                .filter(d -> userRepository.findById(d.getDriverId())
                        .map(User::isAvailable)
                        .orElse(false))
                .min(Comparator.comparingDouble(d ->
                        DistanceUtil.calculateDistance(lat, lng,
                                d.getLatitude(), d.getLongitude())))
                .orElseThrow(() -> new RuntimeException("NO DRIVERS AVAILABLE NEARBY"));

        return userRepository.findById(nearest.getDriverId())
                .orElseThrow(() -> new RuntimeException("DRIVER NOT FOUND"));
    }

    // 📍 Find all nearby drivers within radius (km)
    public List<User> findAllNearby(double lat, double lng, double radiusKm) {

        List<DriverLocation> drivers = locationRepository.findAll();

        return drivers.stream()
                .filter(d -> userRepository.findById(d.getDriverId())
                        .map(User::isAvailable).orElse(false))
                .filter(d -> DistanceUtil.calculateDistance(lat, lng,
                        d.getLatitude(), d.getLongitude()) <= radiusKm)
                .map(d -> userRepository.findById(d.getDriverId()).orElseThrow())
                .collect(Collectors.toList());
    }
}