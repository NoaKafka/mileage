package com.triple.mileage.event.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.triple.mileage.event.data.entity.Review;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.triple.mileage.event.data.entity.QReview.review;


@Repository
public class ReviewRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public ReviewRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        super(Review.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }


    public List<Review> findByReviewId(String reviewId){
        return jpaQueryFactory.selectFrom(review)
                .where(review.reviewId.eq(reviewId))
                .fetch();
    }

    public Long findByPlaceId(String placeId){
        return jpaQueryFactory.selectFrom(review)
                .where(review.placeId.eq(placeId))
                .fetch()
                .stream().count();
    };

    public boolean containUserReview(String placdeId,  String userId) {
        return (jpaQueryFactory.selectFrom(review)
                .where(review.placeId.eq(placdeId))
                .where(review.userId.eq(userId))
                .fetch()
                .stream().count() ) > 0L;
    }

/*

    public List<Post> findAllPostsInnerFetchJoin() {
        return jpaQueryFactory.selectFrom(post)
                .innerJoin(post.comments)
                .fetchJoin()
                .fetch();
    }

    public List<Orphan> findALlOrphans() {
        return jpaQueryFactory.selectFrom(orphan)
                .distinct()
                .leftJoin(orphan.parent).fetchJoin()
                .where(orphan.name.contains("abc"))
                .fetch();
    }

     */
}