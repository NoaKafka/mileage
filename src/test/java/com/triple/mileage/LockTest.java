package com.triple.mileage;


import com.triple.mileage.domain.Place;
import com.triple.mileage.repository.PlaceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class LockTest {

    @Autowired
    PlaceRepository placeRepository;

    @Test
    public void findLock() throws InterruptedException {

        String placeId = "충정로";

        Place place = new Place();
        place.setPlaceId(placeId);
        place.setReviewCnt(0L);

        placeRepository.save(place);

        Optional<Place> dbPlace = placeRepository.findByPlaceIdForUpdate(placeId);

        Optional<Place> dbPlace2 = placeRepository.findByPlaceIdForUpdate(placeId);

        dbPlace.ifPresent( p -> {
            p.setReviewCnt(p.getReviewCnt() + 1L);
            Place n = placeRepository.save(p);
            assertThat(n.getReviewCnt()).isEqualTo(1L);

        });

        dbPlace2.ifPresent( p -> {
            p.setReviewCnt(p.getReviewCnt() + 1L);
            Place n = placeRepository.save(p);
        });

        Optional<Place> dbPlace3 = placeRepository.findByPlaceIdForUpdate(placeId);

        System.out.println(dbPlace3.get().getReviewCnt());
    }
}
