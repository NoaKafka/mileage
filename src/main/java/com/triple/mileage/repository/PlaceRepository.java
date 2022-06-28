package com.triple.mileage.repository;

import com.triple.mileage.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Place p where p.placeId = :placeId")
    Optional<Place> findByPlaceIdForUpdate(@Param("placeId") String placeId);

}
