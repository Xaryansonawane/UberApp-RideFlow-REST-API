package com.example.UberApp.DTOs;

import lombok.*;

public class InvoiceDTO {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InvoiceResponse {

        private Long rideId;

        // 👤 Rider info
        private String riderName;
        private String riderEmail;

        // 🚗 Driver info
        private String driverName;

        // 📍 Locations
        private String pickupLocation;
        private String dropLocation;

        // 💰 Fare breakdown
        private double amount;
        private String paymentMethod;
        private String paymentStatus;

        // ❌ Cancellation info
        private boolean penaltyApplied;
        private String cancelledBy;

        // 📌 Ride status
        private String rideStatus;
    }
}