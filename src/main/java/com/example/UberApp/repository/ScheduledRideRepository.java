package com.example.UberApp.repository;

import com.example.UberApp.model.ScheduledRide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduledRideRepository extends JpaRepository<ScheduledRide, Long> {

    // 📋 All scheduled rides for a rider
    List<ScheduledRide> findByRiderId(Long riderId);

    // 🟡 Pending scheduled rides only
    List<ScheduledRide> findByStatus(String status);
}