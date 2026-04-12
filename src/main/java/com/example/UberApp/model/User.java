package com.example.UberApp.model;

import com.example.UberApp.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @JsonIgnore
    private String password;

    private String phone;

    // 🔐 ROLE (USER / DRIVER / ADMIN)
    @Enumerated(EnumType.STRING)
    private Role role;

    // 💰 Wallet (UI friendly name)
    @JsonProperty("wallet")
    private double walletBalance = 0.0;

    // ❌ Cancellation tracking
    private int cancellationCount = 0;

    // 🟢 Driver availability
    private boolean available = false;

    // 🔒 User active/block status
    @JsonProperty("active")
    private boolean isActive = true;

    private boolean warningGiven = false;

    private LocalDateTime blockedUntil;
}