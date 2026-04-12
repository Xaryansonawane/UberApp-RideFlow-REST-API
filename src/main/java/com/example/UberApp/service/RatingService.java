package com.example.UberApp.service;

import com.example.UberApp.model.Rating;
import com.example.UberApp.model.Ride;
import com.example.UberApp.model.User;
import com.example.UberApp.repository.RatingRepository;
import com.example.UberApp.repository.RideRepository;
import com.example.UberApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final RideRepository rideRepository;

    // ⭐ Give rating after ride completion
    public Rating giveRating(Long ratedById, Long ratedToId, Long rideId,
                             int stars, String comment) {

        // Validate rating range (1–5)
        if (stars < 1 || stars > 5) {
            throw new RuntimeException("STARS MUST BE BETWEEN 1 AND 5");
        }

        // Check ride exists
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("RIDE NOT FOUND"));

        // Prevent duplicate rating for same ride
        if (ratingRepository.existsByRatedByIdAndRideId(ratedById, rideId)) {
            throw new RuntimeException("YOU ALREADY RATED THIS RIDE");
        }

        // Get user who is giving rating
        User ratedBy = userRepository.findById(ratedById)
                .orElseThrow(() -> new RuntimeException("USER NOT FOUND"));

        // Get user who is receiving rating
        User ratedTo = userRepository.findById(ratedToId)
                .orElseThrow(() -> new RuntimeException("USER NOT FOUND"));

        // Create rating object
        Rating rating = new Rating();
        rating.setRatedBy(ratedBy);
        rating.setRatedTo(ratedTo);
        rating.setRide(ride);
        rating.setStars(stars);
        rating.setComment(comment);

        // Save rating
        return ratingRepository.save(rating);
    }

    // 📊 Get average rating of a user
    public double getAverageRating(Long userId) {

        Double avg = ratingRepository.getAverageRating(userId);

        // If no rating exists → return 0
        if (avg == null) return 0.0;

        // Round to 1 decimal (e.g., 4.3)
        return Math.round(avg * 10.0) / 10.0;
    }

    // 📋 Get all ratings of a user
    public List<Rating> getRatingsForUser(Long userId) {
        return ratingRepository.findByRatedToId(userId);
    }
}