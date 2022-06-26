package com.triple.mileage.Repository;

import com.triple.mileage.event.data.entity.LinkPhoto;
import com.triple.mileage.event.data.entity.Review;
import com.triple.mileage.event.data.Event;
import com.triple.mileage.event.repository.LinkPhotoRepository;
import com.triple.mileage.event.repository.ReviewRepository;
import com.triple.mileage.event.repository.ReviewRepositorySupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    void findByReviewId() {
        //given
        List<String> array = new ArrayList<>(Arrays.asList("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2- 851d-4a50-bb07-9cc15cbdc332"));

        Event event = Event.builder()
                .type("REVIEW")
                .action("ADD")
                .reviewId("240a0658-dc5f-4878-9381-ebb7b2667772")
                .content("좋아요!")
                .attachedPhotoIds(array)
                .userId("noakafka")
                .placeId("충정로")
                .build();
        //when
        //save Review
        Review dbReview = reviewRepository.save(Review.builder()
                .reviewId(event.getReviewId())
                .content(event.getContent())
                .userId(event.getUserId())
                .placeId(event.getPlaceId())
                .build()
        );
        for (String photoId : event.getAttachedPhotoIds()) {
            // save link
            linkPhotoRepository.save(LinkPhoto.builder()
                    .photoId(photoId)
                    .review(dbReview)
                    .build()
            );
        }

        List<Review> dbReviews = reviewRepositorySupport.findByReviewId("240a0658-dc5f-4878-9381-ebb7b2667772");

        assertThat(dbReviews.get(0).getReviewId()).isEqualTo("240a0658-dc5f-4878-9381-ebb7b2667772");
    }
}