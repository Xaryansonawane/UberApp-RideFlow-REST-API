package com.example.UberApp.service;

import com.example.UberApp.model.*;
import com.example.UberApp.model.enums.RideStatus;
import com.example.UberApp.repository.*;
import com.example.UberApp.utility.DistanceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RideService {

    private final RideRepository rideRepository;
    private final UserRepository userRepository;
    private final DriverLocationRepository locationRepository;
    private final PaymentService paymentService;
    private final CancellationService cancellationService;

    // Request a new ride
    public Ride requestRide(Long userId, double lat, double lng,
                            String pickup, String drop, double distanceKm) {

        // Find rider from database
        User rider = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("USER NOT FOUND"));

        // Check if user is blocked
        if (cancellationService.isBlocked(userId)) {
            throw new RuntimeException("USER BLOCKED FOR 48 HOURS");
        }

        // Get all driver locations
        List<DriverLocation> driverLocations = locationRepository.findAll();

        // Find nearest available and active driver
        DriverLocation nearestDriver = driverLocations.stream()
                .filter(d -> {
                    Optional<User> driverOpt = userRepository.findById(d.getDriverId());
                    if (driverOpt.isEmpty()) return false;

                    User driver = driverOpt.get();

                    boolean available = driver.isAvailable();
                    boolean active = driver.isActive();
                    boolean notBlocked = driver.getBlockedUntil() == null ||
                            driver.getBlockedUntil().isBefore(LocalDateTime.now());

                    return available && active && notBlocked;
                })
                .min(Comparator.comparingDouble(d ->
                        DistanceUtil.calculateDistance(
                                lat, lng,
                                d.getLatitude(), d.getLongitude()
                        )
                ))
                .orElseThrow(() -> new RuntimeException("NO DRIVERS AVAILABLE"));

        // Get driver details
        User driver = userRepository.findById(nearestDriver.getDriverId())
                .orElseThrow(() -> new RuntimeException("DRIVER NOT FOUND"));

        // Create new ride
        Ride ride = new Ride();
        ride.setRider(rider);
        ride.setDriver(driver);
        ride.setPickupLocation(pickup);
        ride.setDropLocation(drop);
        ride.setStatus(RideStatus.REQUESTED);
        ride.setPenaltyApplied(false);
        ride.setCancelledBy(null);

        // Calculate fare (base + per km)
        double fare = 30 + (distanceKm * 10);
        ride.setFare(fare);

        // Mark driver as busy
        driver.setAvailable(false);
        userRepository.save(driver);

        // Save ride
        return rideRepository.save(ride);
    }

    // Driver accepts the ride
    public Ride acceptRide(Long rideId) {

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("RIDE NOT FOUND"));

        User driver = ride.getDriver();

        // Check if driver is blocked
        if (driver.getBlockedUntil() != null &&
                driver.getBlockedUntil().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("DRIVER BLOCKED");
        }

        // Check if driver is active
        if (!driver.isActive()) {
            throw new RuntimeException("DRIVER INACTIVE");
        }

        // Update ride status
        ride.setStatus(RideStatus.ACCEPTED);

        return rideRepository.save(ride);
    }

    // Complete ride and handle payment
    public Ride completeRide(Long rideId) {

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("RIDE NOT FOUND"));

        // Ride must be accepted first
        if (ride.getStatus() != RideStatus.ACCEPTED) {
            throw new RuntimeException("RIDE MUST BE ACCEPTED FIRST");
        }

        User rider = ride.getRider();
        User driver = ride.getDriver();

        double fare = ride.getFare();

        // Check if user has enough balance
        if (rider.getWalletBalance() < fare) {
            throw new RuntimeException("INSUFFICIENT BALANCE");
        }

        // Deduct money from user
        rider.setWalletBalance(rider.getWalletBalance() - fare);

        // Add money to driver
        driver.setWalletBalance(driver.getWalletBalance() + fare);

        // Make driver available again
        driver.setAvailable(true);

        // Save updated users
        userRepository.save(rider);
        userRepository.save(driver);

        // Mark ride as completed
        ride.setStatus(RideStatus.COMPLETED);

        return rideRepository.save(ride);
    }

    // Cancel ride and apply penalties
    public Map<String, Object> cancelRide(Long rideId) {

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("RIDE NOT FOUND"));

        // Cannot cancel completed ride
        if (ride.getStatus() == RideStatus.COMPLETED) {
            throw new RuntimeException("CANNOT CANCEL COMPLETED RIDE");
        }

        // Update ride status
        ride.setStatus(RideStatus.CANCELLED);

        User user = ride.getRider();
        User driver = ride.getDriver();

        // Apply cancellation logic
        String userMsg = cancellationService.addCancellation(user.getId());
        String driverMsg = cancellationService.driverCancellation(driver.getId());

        // Cancel payment
        paymentService.cancelPayment(rideId, "USER");

        Ride savedRide = rideRepository.save(ride);

        // Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("ride", savedRide);
        response.put("userMessage", userMsg);
        response.put("driverMessage", driverMsg);

        return response;
    }
}