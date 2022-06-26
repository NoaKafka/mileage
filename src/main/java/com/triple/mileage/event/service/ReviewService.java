package com.triple.mileage.event.service;

import com.triple.mileage.event.repository.ReviewRepository;
import com.triple.mileage.event.data.entity.LinkPhoto;
import com.triple.mileage.event.data.entity.Review;
import com.triple.mileage.event.data.Event;
import com.triple.mileage.event.repository.ReviewRepositorySupport;
import com.triple.mileage.point.data.PointLog;
import com.triple.mileage.point.repository.PointLogRepository;
import com.triple.mileage.user.Repository.UserRepository;
import com.triple.mileage.user.data.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@Slf4j
public class  ReviewService {
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewRepositorySupport reviewRepositorySupport;
    private final PointLogRepository pointLogRepository;

    @Autowired
    public ReviewService(UserRepository userRepository,
                         ReviewRepository reviewRepository,
                         ReviewRepositorySupport reviewRepositorySupport,
                         PointLogRepository pointLogRepository) {
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.reviewRepositorySupport = reviewRepositorySupport;
        this.pointLogRepository = pointLogRepository;
    }

    public Review addReview(Event event) {

        // 1. Find User
        User dbUser = userRepository.findByUserId(event.getUserId())
                .orElseThrow(IllegalArgumentException::new);

        // 2. make review Object
        Review review = Review.builder()
                .reviewId(event.getReviewId())
                .content(event.getContent())
                .userId(event.getUserId())
                .placeId(event.getPlaceId())
                .isFirstAtPlace(false)
                .build();

        // 3. calculate Point 1
        Long changeAmount = 0L;
        // 3-1. select by placeId
        if(reviewRepositorySupport.findByPlaceId(event.getPlaceId()) == 0){
            review.setIsFirstAtPlace(true);
            changeAmount += 1L;
        }
        else{
            // 3-2 select by placeId, userId
            if(reviewRepositorySupport.containUserReview(event.getPlaceId(), event.getUserId())){
                return null;
            }
        }
        // 4. calculate Point 2
        // 4-1. photo exist
        if(event.getAttachedPhotoIds().size() != 0) changeAmount += 1L;
        // 4-2. content exist
        if(event.getContent().length() != 0) changeAmount += 1L;


        // 5. save Review
        List<LinkPhoto> linkPhotos = new ArrayList<>();
        for (String photoId : event.getAttachedPhotoIds()) {
            // save link
            linkPhotos.add(LinkPhoto.builder()
                    .photoId(photoId)
                    .review(review)
                    .build()
            );
        }
        review.setLinkPhotos(linkPhotos);

        // 6. make PointLog
        PointLog newPointLog = PointLog.builder()
                .user(dbUser)
                .reviewId(review.getReviewId())
                .amount(changeAmount)
                .action("ADD")
                .build();

        // 7. add Log to user's logList
        dbUser.getPointLogs().add(newPointLog);

        // 8. set Point to User
        dbUser.setPoint(dbUser.getPoint() + changeAmount);

        // 9. save User
        User savedUser = userRepository.save(dbUser);

        return reviewRepository.save(review);
    }

    /** Review 수정
     *
     * @param event
     * @return Review(Modified)
     */
    public Review modifyReview(Event event) {

        // event -> Review
        /** 1. find User*/
        User user = userRepository.findByUserId(event.getUserId())
                .orElseThrow(IllegalArgumentException::new);

        /** 2. find Review */
        Review dbReview = reviewRepository.findByReviewId(event.getReviewId())
                .orElseThrow(IllegalArgumentException::new);

        /** 3. calculate point */
        Long changeAmount = 0L;
        log.info(event.getContent());
        if(dbReview.getLinkPhotos().size() > 0L && event.getAttachedPhotoIds().size() == 0L){changeAmount -= 1L;}
        else if(dbReview.getLinkPhotos().size() == 0L && event.getAttachedPhotoIds().size() > 0L){changeAmount += 1L;}

        log.info("db contents : {} | event contents : {}", dbReview.getContent(), event.getContent());
        if(dbReview.getContent().length() != 0 && event.getContent().length() == 0){changeAmount -= 1L;}
        else if(dbReview.getContent().length() == 0 && event.getContent().length() != 0){changeAmount += 1L;}

        /** 4. save Review */
        List<String> photosToMod = new ArrayList<>(event.getAttachedPhotoIds());

        for(Iterator<LinkPhoto> itr = dbReview.getLinkPhotos().iterator(); itr.hasNext();){
            LinkPhoto dbPhoto = itr.next();
            if(photosToMod.contains(dbPhoto.getPhotoId()) == false){
                //변경 감지
                itr.remove();
            }
        }

        // 새롭게 들어온 photo - > 변경할 photoSet
        for (String photo : photosToMod) {
            LinkPhoto newPhoto = LinkPhoto.builder().photoId(photo).review(dbReview).build();
            if (dbReview.getLinkPhotos().contains(newPhoto) == false) {
                // 변경감지
                dbReview.getLinkPhotos().add(newPhoto);
            }
        }

        //변경감지
        dbReview.setContent(event.getContent());
        Review savedReview =  reviewRepository.save(dbReview);

        /** 5. check Point changed */
        log.info("last changeAmount : {}", changeAmount);
        if(changeAmount != 0L){
            /** 6. make PointLog */
            PointLog newPointLog = PointLog.builder()
                    .user(user)
                    .reviewId(dbReview.getReviewId())
                    .amount(changeAmount)
                    .action("MOD")
                    .build();
            /** 7. add Log to user's logList */
            user.getPointLogs().add(newPointLog);

            /** 8. set Point to User */
            user.setPoint(user.getPoint() + changeAmount);

            /** 9. save User */
            User savedUser = userRepository.save(user);
        }
        return savedReview;
    }


    public void deleteReview(String reviewId, String userId){

        /** 1. find User */
        User user = userRepository.findByUserId(userId)
                .orElseThrow(IllegalArgumentException::new);
        // when
        Optional<Review> dbReview = reviewRepository.findByReviewId(reviewId);

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
                    .action("DELETE")
                    .build();

            /** 5.add Log to user's logList */
            user.getPointLogs().add(newPointLog);
            user.setPoint(user.getPoint() + changeAmount);

            /** 7. save User */
            User savedUser = userRepository.save(user);

            /** 8. delete Review*/
            reviewRepository.delete(selectedReview);
        });
    }
}
