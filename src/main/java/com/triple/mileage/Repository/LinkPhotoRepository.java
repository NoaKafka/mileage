package com.triple.mileage.Repository;

import com.triple.mileage.data.Entity.LinkPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkPhotoRepository extends JpaRepository<LinkPhoto, Long> {

}
