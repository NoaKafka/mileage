package com.triple.mileage.service;

import com.triple.mileage.repository.UserRepository;
import com.triple.mileage.domain.User;
import com.triple.mileage.repository.query.UserDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO findByUserId(UserDTO user){
        Optional<User> dbUser =  userRepository.findByUserId(user.getUserId());
        if(dbUser.isPresent()){
            return new UserDTO(dbUser.get().getUserId(), dbUser.get().getPoint());
        }
        return null;
    }
}
