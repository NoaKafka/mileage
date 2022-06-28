package com.triple.mileage.controller;

import com.triple.mileage.domain.Review;
import com.triple.mileage.repository.query.EventDTO;
import com.triple.mileage.repository.query.ReviewDTO;
import com.triple.mileage.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private final ReviewService reviewService;

    public EventController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ReviewDTO handleEvents(@RequestBody EventDTO eventDTO){

        String action = eventDTO.getAction();
        if(action.equals("ADD")){
            return reviewService.addReview(eventDTO);
        }
        else if(action.equals("MOD")){
            //Review Update
            return reviewService.modifyReview(eventDTO);
        }
        else if(action.equals("DELETE")){
            // Delete Review
            reviewService.deleteReview(eventDTO.getReviewId(), eventDTO.getUserId());
            return null;
        }
        return null;
    }

}
