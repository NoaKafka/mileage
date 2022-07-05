package com.triple.mileage.domain;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
public class LinkPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long linkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REVIEW_ID")
    private Review review;

    @Column(nullable = false)
    private String photoId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkPhoto linkPhoto = (LinkPhoto) o;
        return review.getReviewId().equals(linkPhoto.review.getReviewId()) && photoId.equals(linkPhoto.photoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(review.getReviewId(), photoId);
    }
}
