package com.triple.mileage.user.service;

import com.triple.mileage.user.Repository.UserRepository;
import com.triple.mileage.user.data.entity.User;
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
