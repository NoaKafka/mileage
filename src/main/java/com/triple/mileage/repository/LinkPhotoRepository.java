package com.triple.mileage.repository;

import com.triple.mileage.domain.LinkPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkPhotoRepository extends JpaRepository<LinkPhoto, Long> {

}
