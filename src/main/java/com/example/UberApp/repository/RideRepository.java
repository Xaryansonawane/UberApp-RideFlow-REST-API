package com.example.UberApp.repository;

import com.example.UberApp.model.Ride;
import com.example.UberApp.model.enums.RideStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

    // 👤 All rides of a user
    List<Ride> findByRiderId(Long riderId);

    // 🚗 All rides of a driver
    List<Ride> findByDriverId(Long driverId);

    // 🔍 Rider rides by status
    List<Ride> findByRiderIdAndStatus(Long riderId, RideStatus status);

    // 🔍 Driver rides by status
    List<Ride> findByDriverIdAndStatus(Long driverId, RideStatus status);

    // 🟡 Active ride (REQUESTED / ACCEPTED)
    Optional<Ride> findTopByRiderIdAndStatus(Long riderId, RideStatus status);

    // 📊 Admin: all rides by status
    List<Ride> findByStatus(RideStatus status);

    // 🔥 PRO: Get latest ride of user
    Optional<Ride> findTopByRiderIdOrderByIdDesc(Long riderId);

    // 🔥 PRO: Get latest ride of driver
    Optional<Ride> findTopByDriverIdOrderByIdDesc(Long driverId);
}