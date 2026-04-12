package com.example.UberApp.repository;

import com.example.UberApp.model.Payment;
import com.example.UberApp.model.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // 🔍 Find payment by ride
    Payment findByRideId(Long rideId);

    // 💰 All payments for a driver
    @Query("SELECT p FROM Payment p WHERE p.ride.driver.id = :driverId")
    List<Payment> findByDriverId(Long driverId);

    // ✅ Completed payments for a driver
    @Query("SELECT p FROM Payment p WHERE p.ride.driver.id = :driverId AND p.status = :status")
    List<Payment> findByDriverIdAndStatus(Long driverId, PaymentStatus status);

    // 📊 Total earnings for a driver
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.ride.driver.id = :driverId AND p.status = 'COMPLETED'")
    Double getTotalEarnings(Long driverId);
}