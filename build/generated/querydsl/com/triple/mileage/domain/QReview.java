package com.triple.mileage.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReview is a Querydsl query type for Review
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReview extends EntityPathBase<Review> {

    private static final long serialVersionUID = 2005304699L;

    public static final QReview review = new QReview("review");

    public final StringPath content = createString("content");

    public final BooleanPath isFirstAtPlace = createBoolean("isFirstAtPlace");

    public final ListPath<LinkPhoto, QLinkPhoto> linkPhotos = this.<LinkPhoto, QLinkPhoto>createList("linkPhotos", LinkPhoto.class, QLinkPhoto.class, PathInits.DIRECT2);

    public final StringPath placeId = createString("placeId");

    public final StringPath reviewId = createString("reviewId");

    public final StringPath userId = createString("userId");

    public QReview(String variable) {
        super(Review.class, forVariable(variable));
    }

    public QReview(Path<? extends Review> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReview(PathMetadata metadata) {
        super(Review.class, metadata);
    }

}

