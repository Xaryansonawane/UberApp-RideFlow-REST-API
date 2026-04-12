package com.example.UberApp.DTOs;

import lombok.*;

public class RideDTO {

    @Getter
    @Setter
    public static class RideRequest {
        private Long userId;
        private double latitude;
        private double longitude;
        private String dropLocation;
    }

    @Getter
    @Setter
    public static class RideResponse {
        private Long rideId;
        private String status;
        private String pickup;
        private String drop;
    }
}