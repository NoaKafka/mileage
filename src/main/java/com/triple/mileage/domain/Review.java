package com.triple.mileage.domain;

import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
@Builder
@NoArgsConstructor
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

    @Column(nullable = false)
    private String content;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LinkPhoto> linkPhotos;

    @Column(nullable = false)
    private Boolean isFirstAtPlace = false;

}
