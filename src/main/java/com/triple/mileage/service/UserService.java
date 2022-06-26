package com.triple.mileage.service;

import com.triple.mileage.Repository.UserRepository;
import com.triple.mileage.data.Entity.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUserId(String userId){
        return userRepository.findByUserId(userId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
