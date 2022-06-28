package com.triple.mileage.repository.query;

import lombok.*;

import java.util.List;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private String type;
    private String action;
    private String reviewId;
    private String content;
    private List<String> attachedPhotoIds;
    private String userId;
    private String placeId;
}
