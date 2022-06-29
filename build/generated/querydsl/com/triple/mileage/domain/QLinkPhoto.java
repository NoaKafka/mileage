package com.triple.mileage.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLinkPhoto is a Querydsl query type for LinkPhoto
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLinkPhoto extends EntityPathBase<LinkPhoto> {

    private static final long serialVersionUID = -799267499L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLinkPhoto linkPhoto = new QLinkPhoto("linkPhoto");

    public final NumberPath<Long> linkId = createNumber("linkId", Long.class);

    public final StringPath photoId = createString("photoId");

    public final QReview review;

    public QLinkPhoto(String variable) {
        this(LinkPhoto.class, forVariable(variable), INITS);
    }

    public QLinkPhoto(Path<? extends LinkPhoto> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLinkPhoto(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLinkPhoto(PathMetadata metadata, PathInits inits) {
        this(LinkPhoto.class, metadata, inits);
    }

    public QLinkPhoto(Class<? extends LinkPhoto> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.review = inits.isInitialized("review") ? new QReview(forProperty("review")) : null;
    }

}

