package com.example.UberApp.DTOs;

import lombok.*;

public class DriverDTO {

    @Getter
    @Setter
    public static class LocationUpdateRequest {
        private Long driverId;
        private double latitude;
        private double longitude;
    }

    @Getter
    @Setter
    public static class AvailabilityRequest {
        private Long driverId;
        private boolean status;
    }
}