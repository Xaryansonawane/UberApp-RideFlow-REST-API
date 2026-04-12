package com.example.UberApp.service;

import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    // ⭐ Review add
    public String addReview(Long rideId, int rating, String comment) {

        // future me DB store kar sakte ho
        return "REVIEW ADDED: " + rating + " STARS";
    }
}