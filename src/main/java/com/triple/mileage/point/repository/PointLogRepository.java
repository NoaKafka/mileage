package com.triple.mileage.point.repository;

import com.triple.mileage.point.data.PointLog;
import com.triple.mileage.user.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointLogRepository extends JpaRepository<PointLog, Long> {

    List<PointLog> findByUser(User user);
}
