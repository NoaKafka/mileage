package com.triple.mileage.service;

import com.triple.mileage.exception.NoDataException;
import com.triple.mileage.repository.UserRepository;
import com.triple.mileage.domain.User;
import com.triple.mileage.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO findByUserId(String userId){
        Optional<User> dbUser =  userRepository.findByUserId(userId);
        if(dbUser.isPresent()){
            return new UserDTO(dbUser.get().getUserId(), dbUser.get().getPoint());
        }
        else{
            throw new NoDataException("No User have That Id");
        }
    }
}
