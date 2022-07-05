package com.triple.mileage.domain;

import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@Table(indexes = @Index(name = "reviewIdx", columnList = "placeId, userId"))
public class Review {
    @Id
    @Column(name = "REVIEW_ID")
    private String reviewId;

    @Column(nullable = false)
    private String placeId;

    @Column(nullable = false)
    private String userId;

    @Column
    private String content;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LinkPhoto> linkPhotos;

    @Column(nullable = false)
    private Boolean isFirstAtPlace;

}
