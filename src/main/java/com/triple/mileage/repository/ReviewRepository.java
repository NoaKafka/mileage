package com.triple.mileage.repository;

import com.triple.mileage.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByReviewId(String reviewId);

    void deleteByReviewId(String reviewId);
}
