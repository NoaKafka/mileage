package com.triple.mileage.Repository;

import com.triple.mileage.domain.User;
import com.triple.mileage.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("새로운 유저 등록")
    void addUser(){

        // given
        User user = User.builder()
                .userId("sakarina")
                .point(0L)
                .build();

        // when
        User dbUser = userRepository.save(user);

        // then
        assertThat(dbUser.getUserId()).isEqualTo(user.getUserId());
        assertThat(dbUser.getPoint()).isEqualTo(user.getPoint());


    }

}