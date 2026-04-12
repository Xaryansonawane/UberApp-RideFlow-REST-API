package com.example.UberApp.repository;

import com.example.UberApp.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByDriverId(Long driverId);
}