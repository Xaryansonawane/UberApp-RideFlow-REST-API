package com.example.UberApp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Reference to ride
    @ManyToOne
    private Ride ride;

    // User who gives rating
    @ManyToOne
    private User ratedBy;

    // User who receives rating (driver or rider)
    @ManyToOne
    private User ratedTo;

    // Rating value (1 to 5)
    private int stars;

    // Optional comment
    private String comment;
}