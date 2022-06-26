package com.triple.mileage.data.Entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

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

    @OneToMany(mappedBy = "review", orphanRemoval = true)
    private Set<LinkPhoto> linkPhotos;

}
