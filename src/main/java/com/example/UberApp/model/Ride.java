package com.example.UberApp.model;

import com.example.UberApp.model.enums.RideStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User rider;

    @ManyToOne
    private User driver;

    private String pickupLocation;
    private String dropLocation;

    @Enumerated(EnumType.STRING)
    private RideStatus status;

    private double fare; // 💰 NEW

    private boolean penaltyApplied;

    private String cancelledBy;
}