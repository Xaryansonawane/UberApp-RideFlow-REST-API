package com.example.UberApp.repository;

import com.example.UberApp.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    // ⭐ Get all ratings received by a specific user (driver or rider)
    List<Rating> findByRatedToId(Long userId);

    // ✅ Check if a user has already rated a ride
    boolean existsByRatedByIdAndRideId(Long ratedById, Long rideId);

    // 📊 Calculate average rating for a user
    @Query("SELECT AVG(r.stars) FROM Rating r WHERE r.ratedTo.id = :userId")
    Double getAverageRating(Long userId);
}