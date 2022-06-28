package com.triple.mileage.service;

import com.triple.mileage.repository.LinkPhotoRepositorySupport;
import com.triple.mileage.repository.ReviewRepository;
import com.triple.mileage.domain.LinkPhoto;
import com.triple.mileage.domain.Review;
import com.triple.mileage.repository.query.EventDTO;
import com.triple.mileage.repository.ReviewRepositorySupport;
import com.triple.mileage.domain.PointLog;
import com.triple.mileage.repository.PointLogRepository;
import com.triple.mileage.repository.UserRepository;
import com.triple.mileage.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ReviewServiceTest {

    @Autowired ReviewRepository reviewRepository;
    @Autowired ReviewRepositorySupport reviewRepositorySupport;
    @Autowired UserRepository userRepository;
    @Autowired LinkPhotoRepositorySupport linkPhotoRepositorySupport;
    @Autowired PointLogRepository pointLogRepository;

    @Test
    @Transactional
    @DisplayName("추가")
    void addReviewWithPoint() {
        //given
        List<String> array = new ArrayList<>(Arrays.asList("11", "22", "33"));

        User beforeUser = User.builder().userId("noakafka").point(0L).build();
        userRepository.save(beforeUser);
        EventDTO eventDTO = EventDTO.builder()
                .type("REVIEW")
                .action("ADD")
                .reviewId("review2")
                .content("좋아요!")
                .attachedPhotoIds(array)
                .userId("noakafka")
                .placeId("충정로")
                .build();

        /** 1. Find User */
        User user = userRepository.findByUserId(eventDTO.getUserId())
                .orElseThrow(IllegalArgumentException::new);

        // 2. make review Object
        Review review = Review.builder()
                .reviewId(eventDTO.getReviewId())
                .content(eventDTO.getContent())
                .userId(eventDTO.getUserId())
                .placeId(eventDTO.getPlaceId())
                .isFirstAtPlace(false)
                .build();

        Long changeAmount = 0L;
        // 3. calculate Point 1
        // 3-1. select by placeId
        if(reviewRepositorySupport.findByPlaceId(eventDTO.getPlaceId()) == 0){
            review.setIsFirstAtPlace(true);
            changeAmount += 1L;
        }
        else{
            // 3-2 select by placeId, userId
            if(reviewRepositorySupport.containUserReview(eventDTO.getPlaceId(), eventDTO.getUserId())){
               return;
            }
        }

        // 4. calculate Point 2
        // 4-1. photo exist
        if(eventDTO.getAttachedPhotoIds().size() != 0) changeAmount += 1L;
        // 4-2. content exist
        if(eventDTO.getContent().length() != 0) changeAmount += 1L;

        // 5. save Review

        //when
        List<LinkPhoto> linkPhotos = new ArrayList<>();
        for (String photoId : eventDTO.getAttachedPhotoIds()) {
            // save link
            linkPhotos.add(LinkPhoto.builder()
                    .photoId(photoId)
                    .review(review)
                    .build()
            );
        }
        review.setLinkPhotos(linkPhotos);

        Review dbReview = reviewRepository.save(review);

        /** 6. make PointLog */
        PointLog newPointLog = PointLog.builder()
                .user(user)
                .reviewId(review.getReviewId())
                .amount(changeAmount)
                .action("ADD")
                .build();

        // 7. add Log to user's logList
        List<PointLog> pointLogs = pointLogRepository.findByUser(user);

        pointLogs.add(newPointLog);

        // 8. set Point to User
        user.setPoint(user.getPoint() + changeAmount);
        user.setPointLogs(pointLogs);
        // 9. save User
        User savedUser = userRepository.save(user);

        assertThat(savedUser.getPointLogs().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("추가 + 포인트")
    void addReview() {
        //given
        List<String> array = new ArrayList<>(Arrays.asList("11", "22", "33"));

        EventDTO eventDTO = EventDTO.builder()
                .type("REVIEW")
                .action("ADD")
                .reviewId("review2")
                .content("좋아요!")
                .attachedPhotoIds(array)
                .userId("noakafka")
                .placeId("충정로")
                .build();
        Review review = Review.builder()
                .reviewId(eventDTO.getReviewId())
                .content(eventDTO.getContent())
                .userId(eventDTO.getUserId())
                .placeId(eventDTO.getPlaceId())
                .build();

        //when
        List<LinkPhoto> linkPhotos = new ArrayList<>();
        for (String photoId : eventDTO.getAttachedPhotoIds()) {
            // save link
            linkPhotos.add(LinkPhoto.builder()
                    .photoId(photoId)
                    .review(review)
                    .build()
            );
        }
        review.setLinkPhotos(linkPhotos);
        //save Review
        Review dbReview = reviewRepository.save(review);
    }

    @Test
    @DisplayName("수정 -> Transactional로 해야 영속성컨텍스트 유지")
    void modifyReviewWithPoint() {
        // given
        Review reqReview = Review.builder()
                .reviewId("review1")
                .content("hi")
                .userId("noakafka")
                .placeId("충정로")
                .build();

        /** 1. find User*/
        User user = userRepository.findByUserId(reqReview.getUserId())
                .orElseThrow(IllegalArgumentException::new);

        /** 2. find Review */
        Review dbReview = reviewRepository.findByReviewId(reqReview.getReviewId())
                .orElseThrow(IllegalArgumentException::new);

        Long changeAmount = 0L;
        /** 3. calculate point */
        if(dbReview.getLinkPhotos().size() != 0 && reqReview.getLinkPhotos().size() == 0){changeAmount -= 1L;}
        else if(dbReview.getLinkPhotos().size() == 0 && reqReview.getLinkPhotos().size() != 0){changeAmount += 1L;}

        /** 4. save Review */
        List<String> photosToMod = new ArrayList<>();
        photosToMod.add("cascade1");photosToMod.add("cascade2");

        List<LinkPhoto> dbLinks = linkPhotoRepositorySupport.findByReviewId(dbReview.getReviewId());

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

        /** 5. check Point changed */
        if(changeAmount > 0){
            /** 6. make PointLog */
            // 6. make PointLog
            PointLog newPointLog = PointLog.builder()
                    .user(user)
                    .reviewId(dbReview.getReviewId())
                    .amount(changeAmount)
                    .action("ADD")
                    .build();
            /** 7. add Log to user's logList */
            List<PointLog> pointLogs = pointLogRepository.findByUser(user);

            pointLogs.add(newPointLog);

            /** 8. set Point to User */
            user.setPoint(user.getPoint() + changeAmount);
            user.setPointLogs(pointLogs);
            /** 9. save User */
            User savedUser = userRepository.save(user);
        }

        assertThat(changedReview.getContent()).isEqualTo(reqReview.getContent());
    }

    @Test
    @DisplayName("수정")
    @Transactional
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

        List<String> photosToMod = new ArrayList<>();
        photosToMod.add("cascade2");photosToMod.add("11");

        List<LinkPhoto> dbLinks = linkPhotoRepositorySupport.findByReviewId(dbReview.getReviewId());

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
    @DisplayName("삭제 + 포인트")
    void deleteReviewWithPoint(){

        // given
        Review reqReview = Review.builder()
                .reviewId("review1")
                .content("hi")
                .userId("noakafka")
                .placeId("충정로")
                .isFirstAtPlace(false)
                .build();

        /** 1. find User */
        User user = userRepository.findByUserId(reqReview.getUserId())
                .orElseThrow(IllegalArgumentException::new);

        /** 2. find Review */
        Optional<Review> dbReview = reviewRepository.findByReviewId(reqReview.getReviewId());

        dbReview.ifPresent(selectedReview -> {
            /** 3. calculate Point */
            Long changeAmount = 0L;
            if(selectedReview.getIsFirstAtPlace()) changeAmount -= 1L;
            if(selectedReview.getContent().length() > 0L) changeAmount -= 1L;
            if(selectedReview.getLinkPhotos().size() > 0L) changeAmount -= 1L;

            /** 4. make PointLog */
            PointLog newPointLog = PointLog.builder()
                    .user(user)
                    .reviewId(selectedReview.getReviewId())
                    .amount(changeAmount)
                    .action("ADD")
                    .build();

            /** 5.add Log to user's logList */
            List<PointLog> pointLogs = pointLogRepository.findByUser(user);
            pointLogs.add(newPointLog);

            /** 6. set Point to User */
            user.setPoint(user.getPoint() + changeAmount);
            user.setPointLogs(pointLogs);

            /** 7. save User */
            User savedUser = userRepository.save(user);

            /** 8. delete Review*/
            reviewRepository.delete(selectedReview);
        });
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