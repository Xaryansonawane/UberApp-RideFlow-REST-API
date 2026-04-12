package com.example.UberApp.DTOs;

import lombok.*;

public class ReviewDTO {

    @Getter
    @Setter
    public static class ReviewRequest {
        private Long rideId;
        private int rating;
        private String comment;
    }
}