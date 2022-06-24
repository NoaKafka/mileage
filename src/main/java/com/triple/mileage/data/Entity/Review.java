package com.triple.mileage.data.Entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Builder
@Table(name = "Review")
public class Review {
    @Id
    @Column(nullable = false)
    private String reviewId;

    @Column(nullable = false)
    private String placeId;

    @Column(nullable = false)
    private String userId;

    @Column
    private String content;
}
