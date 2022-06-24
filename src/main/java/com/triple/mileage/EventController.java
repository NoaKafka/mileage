package com.triple.mileage;

import com.triple.mileage.data.Event;
import com.triple.mileage.service.ReviewService;
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
    public String AcceptEvents(@RequestBody Event event){
        String result = reviewService.addReview(event);
        log.info(result);
        return event.toString();
    }

}
