package com.triple.mileage.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.triple.mileage.data.Entity.LinkPhoto;
import com.triple.mileage.data.Entity.QLinkPhoto;
import com.triple.mileage.data.Entity.Review;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.triple.mileage.data.Entity.QLinkPhoto.linkPhoto;
import static com.triple.mileage.data.Entity.QReview.review;

@Repository
public class LinkPhotoRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    public LinkPhotoRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        super(LinkPhoto.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Set<LinkPhoto> findByReviewId(String reviewId){
        List<LinkPhoto> result = jpaQueryFactory.selectFrom(linkPhoto)
                .where(linkPhoto.review.reviewId.eq(review.reviewId))
                .fetch();

        return new HashSet<>(result);
    }

}
