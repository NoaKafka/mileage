package com.triple.mileage.service;

import com.triple.mileage.domain.Place;
import com.triple.mileage.repository.PlaceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PlaceServiceTest {


    @Autowired
    PlaceRepository placeRepository;

    @Test
    public void addPlace(){

        Place place = new Place();
        place.setPlaceId("충정로");
        place.setReviewCnt(0L);

        Place dbPlace = placeRepository.save(place);
        assertThat(dbPlace.getPlaceId()).isEqualTo("충정로");
    }
}
