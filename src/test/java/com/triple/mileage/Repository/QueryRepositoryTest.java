package com.triple.mileage.Repository;

import com.triple.mileage.domain.LinkPhoto;
import com.triple.mileage.domain.Review;
import com.triple.mileage.dto.EventDTO;
import com.triple.mileage.repository.LinkPhotoRepository;
import com.triple.mileage.repository.ReviewRepository;
import com.triple.mileage.repository.ReviewRepositorySupport;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class QueryRepositoryTest {

    @Autowired
    ReviewRepositorySupport reviewRepositorySupport;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    LinkPhotoRepository linkPhotoRepository;

    @Test
    @DisplayName("리뷰 목록 조회")
    @Transactional
    void findByReviewId() {
        //given
        List<String> array = new ArrayList<>(Arrays.asList("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2- 851d-4a50-bb07-9cc15cbdc332"));

        EventDTO eventDTO = new EventDTO("REVIEW",
                "ADD",
                "review2",
                "좋아요!",
                array,
                "noakafka",
                "충정로");
        //when
        //save Review
        Review dbReview = reviewRepository.save(Review.builder()
                .reviewId(eventDTO.getReviewId())
                .content(eventDTO.getContent())
                .userId(eventDTO.getUserId())
                .placeId(eventDTO.getPlaceId())
                .isFirstAtPlace(false)
                .build()
        );
        for (String photoId : eventDTO.getAttachedPhotoIds()) {
            // save link
            linkPhotoRepository.save(LinkPhoto.builder()
                    .photoId(photoId)
                    .review(dbReview)
                    .build()
            );
        }

        List<Review> dbReviews = reviewRepositorySupport.findByReviewId("review2");

        assertThat(dbReviews.get(0).getReviewId()).isEqualTo("review2");
    }
}