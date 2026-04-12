package com.example.UberApp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduledRide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 👤 Who booked it
    @ManyToOne
    private User rider;

    private String pickupLocation;
    private String dropLocation;

    // 🕐 When to start the ride
    private LocalDateTime scheduledTime;

    // 📌 Status: PENDING / CONFIRMED / CANCELLED
    private String status;
}