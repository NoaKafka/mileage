package com.triple.mileage.data.Entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "ReviewImage")
public class ReviewImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(nullable = false)
    private String reviewId;

    @Column(nullable = false)
    private String imageId;
}
