package com.triple.mileage.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.triple.mileage.data.Entity.Review;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.triple.mileage.data.Entity.QReview.*;

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