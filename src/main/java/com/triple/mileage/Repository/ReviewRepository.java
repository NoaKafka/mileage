package com.triple.mileage.Repository;

import com.triple.mileage.data.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByReviewId(String reviewId);

    void deleteByReviewId(String reviewId);


}
