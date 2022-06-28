package com.triple.mileage.service;

import com.triple.mileage.repository.UserRepository;
import com.triple.mileage.domain.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUserId(String userId){
        Optional<User> dbUser =  userRepository.findByUserId(userId);

        if(dbUser.isPresent()){
            return dbUser.get();
        }
        return null;
    }
}
