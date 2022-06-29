package com.triple.mileage.service;

import com.triple.mileage.domain.*;
import com.triple.mileage.exception.AlreadyWrittenException;
import com.triple.mileage.exception.NoDataException;
import com.triple.mileage.repository.*;
import com.triple.mileage.dto.EventDTO;
import com.triple.mileage.dto.ReviewDTO;
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
    public ReviewDTO addReview(EventDTO eventDTO) {
        // 1. Find User
        Optional<User> optionalUser = userRepository.findByUserId(eventDTO.getUserId());
        if(optionalUser.isPresent() == false) userRepository.save(User.builder()
                .userId(eventDTO.getUserId())
                .pointLogs(new ArrayList<PointLog>())
                .point(0L).build());

        User dbUser = getDbUser(eventDTO.getUserId());

        // 2. make review Object
        Review review = Review.builder()
                .reviewId(eventDTO.getReviewId())
                .content(eventDTO.getContent())
                .userId(eventDTO.getUserId())
                .placeId(eventDTO.getPlaceId())
                .isFirstAtPlace(false)
                .build();

        /** 장소가 존재하지 않으면 DB에 추가 */
        Optional<Place> place = placeRepository.findByPlaceIdForUpdate(eventDTO.getPlaceId());
        if (place.isPresent() == false) {
            placeRepository.save(Place.builder()
                    .placeId(eventDTO.getPlaceId())
                    .reviewCnt(0L).build());
        }
        else{
            placeRepository.save(place.get());
        }

        // 3. calculate Point 1
        Long changeAmount = 0L;
        /** 이 장소에 내 댓글이 없는가? */
        // 3-1 select by placeId, userId
        existUserReview(eventDTO.getPlaceId(), eventDTO.getUserId());

        /** 이 장소에 댓글 갯수가 0개인가? */
        // 3-2. select by placeId
        Place dbPlace = getDbPlace(eventDTO.getPlaceId());

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
        List<LinkPhoto> linkPhotos = makePhotoList(eventDTO.getAttachedPhotoIds(), review);
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
        userRepository.save(dbUser);
        Review savedReivew = reviewRepository.save(review);

        /** make DTO */
        List<String> dtoPhotoList = new ArrayList<>();
        for (LinkPhoto linkPhoto : savedReivew.getLinkPhotos()) {
            dtoPhotoList.add(linkPhoto.getPhotoId());
        }

        return new ReviewDTO(savedReivew.getReviewId(),
                savedReivew.getPlaceId(),
                savedReivew.getUserId(),
                savedReivew.getContent(),
                dtoPhotoList,
                savedReivew.getIsFirstAtPlace());
    }

    /** Review 수정
     *
     * @param eventDTO
     * @return Review(Modified)
     */
    @Transactional
    public ReviewDTO modifyReview(EventDTO eventDTO) {

        // event -> Review
        /** 1. find User*/
        User user = getDbUser(eventDTO.getUserId());

        /** 2. find Review */
        Review dbReview = getDbReview(eventDTO.getReviewId());

        /** 3. calculate point */
        Long changeAmount = 0L;
        if(dbReview.getLinkPhotos().size() > 0L && eventDTO.getAttachedPhotoIds().size() == 0L){changeAmount -= 1L;}
        else if(dbReview.getLinkPhotos().size() == 0L && eventDTO.getAttachedPhotoIds().size() > 0L){changeAmount += 1L;}

        if(dbReview.getContent().length() > 0L && eventDTO.getContent().length() == 0L){changeAmount -= 1L;}
        else if(dbReview.getContent().length() == 0L && eventDTO.getContent().length() >= 0L){changeAmount += 1L;}

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
        List<String> dtoPhotoList = new ArrayList<>();
        for (LinkPhoto linkPhoto : savedReview.getLinkPhotos()) {
            dtoPhotoList.add(linkPhoto.getPhotoId());
        }
        return new ReviewDTO(savedReview.getReviewId(),
                savedReview.getPlaceId(),
                savedReview.getUserId(),
                savedReview.getContent(),
                dtoPhotoList,
                savedReview.getIsFirstAtPlace());
    }


    @Transactional
    public void deleteReview(String reviewId, String userId){

        /** 1. find User & Review*/
        User user = getDbUser(userId);
        // when
        Review dbReview = getDbReview(reviewId);

        /** 2. calculate place's reviewCnt */
        Place dbPlace = getDbPlace(dbReview.getPlaceId());

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

    private Place getDbPlace(String placeId) {
        Optional<Place> place =  placeRepository.findByPlaceIdForUpdate(placeId);
        if(place.isPresent()){
            return place.get();
        }
        else{
            throw new NoDataException("No Place Have That Id");
        }
    }

    private Review getDbReview(String reviewId) {
        Optional<Review> review =  reviewRepository.findByReviewId(reviewId);
        if(review.isPresent()){
            return review.get();
        }
        else{
            throw new NoDataException("No Review Have That Id");
        }
    }

    private List<LinkPhoto> makePhotoList(List<String> attachedPhotoIds, Review review) {
        List<LinkPhoto> photoList = new ArrayList<>();
        for (String photoId : attachedPhotoIds) {
            // save link
            photoList.add(LinkPhoto.builder()
                    .photoId(photoId)
                    .review(review)
                    .build()
            );
        }
        return photoList;
    }

    private User getDbUser(String userId){
        Optional<User> user = userRepository.findByUserId(userId);
        if(user.isPresent()){
            return user.get();
        }else{
            throw new NoDataException("No User Have That Name");
        }
    }

    private void existUserReview(String placeId, String userId) {
        if(reviewRepositorySupport.containUserReview(placeId, userId)){
            throw new AlreadyWrittenException("User Already have written review at this place");
        }
    }
}
