package com.triple.mileage.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.triple.mileage.domain.LinkPhoto;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.triple.mileage.domain.QLinkPhoto.linkPhoto;
import static com.triple.mileage.domain.QReview.review;


@Repository
public class LinkPhotoRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    public LinkPhotoRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        super(LinkPhoto.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<LinkPhoto> findByReviewId(String reviewId){
        return jpaQueryFactory.selectFrom(linkPhoto)
                .where(linkPhoto.review.reviewId.eq(review.reviewId))
                .fetch();
    }

}
