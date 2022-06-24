package com.triple.mileage.Repository;

import com.triple.mileage.data.Entity.Review;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    @DisplayName("새 Review 저장")
    public void addNewMember(){
        //given
        Review review = Review.builder()
                .reviewId("id-1234")
                .content("좋아요!")
                .userId("noakafka")
                .placeId("seoul")
                .build();
        //when
        Review dbReview = reviewRepository.save(review);

        //then
        assertThat(dbReview.getReviewId()).isEqualTo(review.getReviewId());
        assertThat(dbReview.getContent()).isEqualTo(review.getContent());
        assertThat(dbReview.getUserId()).isEqualTo(review.getUserId());

    }

    @Test
    void findByReviewId() {
    }
}