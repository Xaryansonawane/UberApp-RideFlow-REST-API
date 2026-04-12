package com.example.UberApp.model;

import com.example.UberApp.model.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Ride ride;

    private double amount;

    // WALLET / CASH / UPI
    private String method;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    // ❌ cancel tracking
    private boolean penaltyApplied;
    private String cancelledBy;
}