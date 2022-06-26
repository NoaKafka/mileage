package com.triple.mileage.user.service;

import com.triple.mileage.point.data.PointLog;
import com.triple.mileage.user.Repository.UserRepository;
import com.triple.mileage.user.data.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("유저 추가")
    void addUser(){

        // given
        List<PointLog> logList = new ArrayList<>();
        User user = User.builder()
                .userId("noakafka")
                .point(0L)
                .pointLogs(logList)
                .build();
        // when
        User dbUser = userRepository.save(user);

        // then
        assertThat(dbUser.getUserId()).isEqualTo(user.getUserId());


    }

}