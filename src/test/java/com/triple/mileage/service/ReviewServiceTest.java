package com.triple.mileage.service;

import com.triple.mileage.Repository.ReviewRepository;
import com.triple.mileage.data.Entity.Review;
import com.triple.mileage.data.Event;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@SpringBootTest
class ReviewServiceTest {


    @Autowired
    private ReviewRepository reviewRepository;
    private final ReviewService reviewService = new ReviewService(reviewRepository);

    @Test
    void addReview() {
        //given
        List<String> array = new ArrayList<>(Arrays.asList("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2- 851d-4a50-bb07-9cc15cbdc332"));

        Event event = Event.builder()
                .type("REVIEW")
                .action("ADD")
                .reviewId("240a0658-dc5f-4878-9381-ebb7b2667772")
                .content("좋아요!")
                .attachedPhotoIds(array)
                .userId("noakafka")
                .placeId("충정로")
                .build();
        //when
        String result = reviewService.addReview(event);
        //then
        Assertions.assertThat(result).isEqualTo("add test");
    }
}