package com.triple.mileage.Repository;

import com.triple.mileage.domain.LinkPhoto;
import com.triple.mileage.domain.Review;
import com.triple.mileage.repository.LinkPhotoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LinkPhotoRepositoryTest {

    @Autowired private LinkPhotoRepository linkPhotoRepository;

    @Test
    @DisplayName("새 Review 저장")
    void addNewMember(){
        //given
        Review dbReview = Review.builder()
                .reviewId("review")
                .content("hi")
                .userId("noakafka")
                .placeId("충정로")
                .build();
        LinkPhoto linkPhoto = LinkPhoto.builder().review(dbReview).photoId("cascade1").build();;
        //when
        LinkPhoto dbLinkPhoto = linkPhotoRepository.save(linkPhoto);
        //then
        assertThat(dbLinkPhoto.getReview().getReviewId()).isEqualTo(linkPhoto.getReview().getReviewId());
        assertThat(dbLinkPhoto.getPhotoId()).isEqualTo(linkPhoto.getPhotoId());

    }
}