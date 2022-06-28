package com.triple.mileage.repository.query;

import com.triple.mileage.domain.LinkPhoto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ReviewDTO {
    private String reviewId;
    private String placeId;
    private String userId;
    private String content;
    private List<String> linkPhotos;
    private Boolean isFirstAtPlace;
}
