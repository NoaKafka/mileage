package com.triple.mileage.event.controller;

import com.triple.mileage.event.data.entity.Review;
import com.triple.mileage.event.data.Event;
import com.triple.mileage.event.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String handleEvents(@RequestBody Event event){

        String action = event.getAction();
        if(action.equals("ADD")){
            Review review = reviewService.addReview(event);
        }
        else if(action.equals("MOD")){
            //Review Update
            Review review = reviewService.modifyReview(event);
        }
        else if(action.equals("DELETE")){
            // Delete Review
            reviewService.deleteReview(event.getReviewId(), event.getUserId());
        }
        return "";
    }

}
