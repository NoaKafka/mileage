package com.triple.mileage.service;

import com.triple.mileage.Repository.LinkPhotoRepository;
import com.triple.mileage.Repository.LinkPhotoRepositorySupport;
import com.triple.mileage.Repository.ReviewRepository;
import com.triple.mileage.data.Entity.LinkPhoto;
import com.triple.mileage.data.Entity.Review;
import com.triple.mileage.data.Event;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ReviewServiceTest {

    @Autowired ReviewRepository reviewRepository;
    @Autowired LinkPhotoRepository linkPhotoRepository;
    @Autowired LinkPhotoRepositorySupport linkPhotoRepositorySupport;
    @Autowired ReviewService reviewService;


    @Test
    @DisplayName("추가")
    void addReview() {
        //given
        List<String> array = new ArrayList<>(Arrays.asList("11", "22", "33"));

        Event event = Event.builder()
                .type("REVIEW")
                .action("ADD")
                .reviewId("review2")
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
    }

    @Test
    @DisplayName("수정")
    void modifyReview() {
        // given
        Review reqReview = Review.builder()
                .reviewId("review2")
                .content("hi")
                .userId("noakafka")
                .placeId("충정로")
                .build();
        // when
        Review dbReview = reviewRepository.findByReviewId(reqReview.getReviewId())
                .orElseThrow(IllegalArgumentException::new);

        Set<String> photosToMod = new HashSet<>();
        photosToMod.add("cascade2");photosToMod.add("11");

        Set<LinkPhoto> dbLinks = linkPhotoRepositorySupport.findByReviewId(dbReview.getReviewId());

        for(Iterator<LinkPhoto> itr = dbLinks.iterator(); itr.hasNext();){
            LinkPhoto dbPhoto = itr.next();
            if(photosToMod.contains(dbPhoto.getPhotoId()) == false){
                itr.remove();
            }
        }
        // 새롭게 들어온 photo - > 변경할 photoSet
        for (String photo : photosToMod) {
            dbLinks.add(LinkPhoto.builder().photoId(photo).review(reqReview).build());
        }

        //변경감지
        dbReview.setContent(reqReview.getContent());
        dbReview.setLinkPhotos(dbLinks);
        Review changedReview = reviewRepository.save(dbReview);

        // then
        //assertThat(dbReview.getLinkPhotos().size()).isEqualTo(photosToMod.size());
        assertThat(changedReview.getContent()).isEqualTo(reqReview.getContent());
    }

    @Test
    @DisplayName("삭제")
    void deleteReview(){

        // given
        Review reqReview = Review.builder()
                .reviewId("review2")
                .content("hi")
                .userId("noakafka")
                .placeId("충정로")
                .build();

        // when
        Optional<Review> dbReview = reviewRepository.findByReviewId(reqReview.getReviewId());
        dbReview.ifPresent(selectedReview -> {
            reviewRepository.delete(selectedReview);
        });
    }
}