package com.triple.mileage.controller;

import com.triple.mileage.dto.EventDTO;
import com.triple.mileage.dto.ReviewDTO;
import com.triple.mileage.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity handleEvents(@RequestBody EventDTO eventDTO){

        String action = eventDTO.getAction();
        if(action.equals("ADD")){
            ReviewDTO dbReview = reviewService.addReview(eventDTO);
            return ResponseEntity.ok(dbReview);
        }
        else if(action.equals("MOD")){
            //Review Update
            ReviewDTO dbReview = reviewService.modifyReview(eventDTO);
            return ResponseEntity.ok(dbReview);
        }
        else if(action.equals("DELETE")){
            // Delete Review
            reviewService.deleteReview(eventDTO.getReviewId(), eventDTO.getUserId());
            return ResponseEntity.ok().build();
        }
        return null;
    }

}
