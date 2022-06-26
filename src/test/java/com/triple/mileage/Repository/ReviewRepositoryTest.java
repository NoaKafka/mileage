package com.triple.mileage.Repository;

import com.triple.mileage.event.data.entity.Review;
import com.triple.mileage.event.repository.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ReviewRepositoryTest {

    @Test
    void findByReviewId() {
    }

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    @DisplayName("새 Review 저장")
    public void addNewReview(){
        //given
        Review review = Review.builder()
                .reviewId("id-1237")
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
}