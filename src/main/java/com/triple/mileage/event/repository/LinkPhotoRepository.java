package com.triple.mileage.event.repository;

import com.triple.mileage.event.data.entity.LinkPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkPhotoRepository extends JpaRepository<LinkPhoto, Long> {

}
