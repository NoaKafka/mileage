package com.triple.mileage.event.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.triple.mileage.event.data.entity.LinkPhoto;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.triple.mileage.event.data.entity.QLinkPhoto.linkPhoto;
import static com.triple.mileage.event.data.entity.QReview.review;


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
