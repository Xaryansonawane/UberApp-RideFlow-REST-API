package com.example.UberApp.repository;

import com.example.UberApp.model.LocationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationHistoryRepository extends JpaRepository<LocationHistory, Long> {

    List<LocationHistory> findByDriverId(Long driverId);
}