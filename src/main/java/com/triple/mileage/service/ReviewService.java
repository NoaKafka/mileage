package com.triple.mileage.service;

import com.triple.mileage.Repository.LinkPhotoRepository;
import com.triple.mileage.Repository.LinkPhotoRepositorySupport;
import com.triple.mileage.Repository.ReviewRepository;
import com.triple.mileage.data.Entity.LinkPhoto;
import com.triple.mileage.data.Entity.Review;
import com.triple.mileage.data.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.*;


@Service
@Slf4j
public class  ReviewService {

    private final ReviewRepository reviewRepository;
    private final LinkPhotoRepository linkPhotoRepository;
    private final LinkPhotoRepositorySupport linkPhotoRepositorySupport;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, LinkPhotoRepository linkPhotoRepository, LinkPhotoRepositorySupport linkPhotoRepositorySupport) {
        this.reviewRepository = reviewRepository;
        this.linkPhotoRepository = linkPhotoRepository;
        this.linkPhotoRepositorySupport = linkPhotoRepositorySupport;
    }

    public Review addReview(Event event) {
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
        return dbReview;
    }

    /** Review 수정
     *
     * @param event
     * @return Review(Modified)
     */
    public Review modifyReview(Event event) {

        // event -> Review
        Review reqReview = Review.builder()
                .reviewId(event.getReviewId())
                .content(event.getContent())
                .userId(event.getPlaceId())
                .placeId(event.getPlaceId())
                .build();

        Review dbReview = reviewRepository.findByReviewId(reqReview.getReviewId())
                .orElseThrow(IllegalArgumentException::new);

        Set<String> photosToMod = new HashSet<>(event.getAttachedPhotoIds());


        for(Iterator<LinkPhoto> itr = dbReview.getLinkPhotos().iterator(); itr.hasNext();){
            LinkPhoto dbPhoto = itr.next();
            if(photosToMod.contains(dbPhoto.getPhotoId()) == false){
                //변경 감지
                itr.remove();
            }
        }

        // 새롭게 들어온 photo - > 변경할 photoSet
        for (String photo : photosToMod) {
            // 변경감지
            dbReview.getLinkPhotos().add(LinkPhoto.builder().photoId(photo).review(reqReview).build());
        }

        //변경감지
        dbReview.setContent(reqReview.getContent());

        return reviewRepository.save(dbReview);
    }


    public void deleteReview(String reviewId){
        // when
        Optional<Review> dbReview = reviewRepository.findByReviewId(reviewId);
        dbReview.ifPresent(selectedReview -> {
            reviewRepository.delete(selectedReview);
        });
    }
}
