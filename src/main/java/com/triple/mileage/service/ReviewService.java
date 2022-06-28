package com.triple.mileage.service;

import com.triple.mileage.domain.*;
import com.triple.mileage.repository.*;
import com.triple.mileage.repository.query.EventDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Slf4j
public class  ReviewService {
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewRepositorySupport reviewRepositorySupport;
    private final PlaceRepository placeRepository;

    @Autowired
    public ReviewService(UserRepository userRepository,
                         ReviewRepository reviewRepository,
                         ReviewRepositorySupport reviewRepositorySupport,
                         PlaceRepository placeRepository) {
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.reviewRepositorySupport = reviewRepositorySupport;
        this.placeRepository = placeRepository;
    }

    @Transactional
    public Review addReview(EventDTO eventDTO) {

        // 1. Find User
        User dbUser = userRepository.findByUserId(eventDTO.getUserId())
                .orElseThrow(IllegalArgumentException::new);

        // 2. make review Object
        Review review = Review.builder()
                .reviewId(eventDTO.getReviewId())
                .content(eventDTO.getContent())
                .userId(eventDTO.getUserId())
                .placeId(eventDTO.getPlaceId())
                .isFirstAtPlace(false)
                .build();

        // 3. calculate Point 1
        Long changeAmount = 0L;
        /** 이 장소에 내 댓글이 없는가? */
        // 3-1 select by placeId, userId
        if(reviewRepositorySupport.containUserReview(eventDTO.getPlaceId(), eventDTO.getUserId())){
            return null;
        }
        /** 이 장소에 댓글 갯수가 0개인가? */
        // 3-2. select by placeId
        Place dbPlace = placeRepository.findByPlaceIdForUpdate(eventDTO.getPlaceId())
                .orElseThrow(IllegalArgumentException::new);


        Long cntReview = dbPlace.getReviewCnt();
        if(cntReview == 0L) {
            dbUser.setPoint(dbUser.getPoint() + 1L);
            review.setIsFirstAtPlace(true);
        }
        dbPlace.setReviewCnt(cntReview + 1L);
        /** Lock 해제*/
        placeRepository.save(dbPlace);

        // 4. calculate Point 2
        // 4-1. photo exist
        if(eventDTO.getAttachedPhotoIds().size() != 0) changeAmount += 1L;
        // 4-2. content exist
        if(eventDTO.getContent().length() != 0) changeAmount += 1L;

        // 5. save Review
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
     * @param eventDTO
     * @return Review(Modified)
     */
    @Transactional
    public Review modifyReview(EventDTO eventDTO) {

        // event -> Review
        /** 1. find User*/
        User user = userRepository.findByUserId(eventDTO.getUserId())
                .orElseThrow(IllegalArgumentException::new);

        /** 2. find Review */
        Review dbReview = reviewRepository.findByReviewId(eventDTO.getReviewId())
                .orElseThrow(IllegalArgumentException::new);

        /** 3. calculate point */
        Long changeAmount = 0L;
        log.info(eventDTO.getContent());
        if(dbReview.getLinkPhotos().size() > 0L && eventDTO.getAttachedPhotoIds().size() == 0L){changeAmount -= 1L;}
        else if(dbReview.getLinkPhotos().size() == 0L && eventDTO.getAttachedPhotoIds().size() > 0L){changeAmount += 1L;}

        log.info("db contents : {} | event contents : {}", dbReview.getContent(), eventDTO.getContent());
        if(dbReview.getContent().length() != 0 && eventDTO.getContent().length() == 0){changeAmount -= 1L;}
        else if(dbReview.getContent().length() == 0 && eventDTO.getContent().length() != 0){changeAmount += 1L;}

        /** 4. save Review */
        List<String> photosToMod = new ArrayList<>(eventDTO.getAttachedPhotoIds());

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
        dbReview.setContent(eventDTO.getContent());
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


    @Transactional
    public void deleteReview(String reviewId, String userId){

        /** 1. find User */
        User user = userRepository.findByUserId(userId)
                .orElseThrow(IllegalArgumentException::new);
        // when
        Review dbReview = reviewRepository.findByReviewId(reviewId)
                .orElseThrow(IllegalArgumentException::new);

        /** 2. calculate place's reviewCnt */
        Place dbPlace = placeRepository.findByPlaceIdForUpdate(dbReview.getPlaceId())
                .orElseThrow(IllegalArgumentException::new);

        dbPlace.setReviewCnt(dbPlace.getReviewCnt() - 1L);
        placeRepository.save(dbPlace);

        /** 3. calculate Point */
        Long changeAmount = 0L;
        if(dbReview.getIsFirstAtPlace()) changeAmount -= 1L;
        if(dbReview.getContent().length() > 0L) changeAmount -= 1L;
        if(dbReview.getLinkPhotos().size() > 0L) changeAmount -= 1L;

        /** 4. make PointLog */
        PointLog newPointLog = PointLog.builder()
                .user(user)
                .reviewId(dbReview.getReviewId())
                .amount(changeAmount)
                .action("DELETE")
                .build();

        /** 5.add Log to user's logList */
        user.getPointLogs().add(newPointLog);
        user.setPoint(user.getPoint() + changeAmount);

        /** 7. save User */
        User savedUser = userRepository.save(user);

        /** 8. delete Review*/
        reviewRepository.delete(dbReview);
    }
}