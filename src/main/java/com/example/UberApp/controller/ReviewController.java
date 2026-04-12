package com.example.UberApp.controller;

import com.example.UberApp.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // ⭐ User driver ko rating dega
    @PostMapping("/add")
    public String addReview(
            @RequestParam Long rideId,
            @RequestParam int rating,
            @RequestParam String comment
    ) {
        return reviewService.addReview(rideId, rating, comment);
    }
}