package com.triple.mileage.event.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @Column
    private Boolean isFirstAtPlace;

}
