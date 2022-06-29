package com.triple.mileage.service;

import com.triple.mileage.domain.PointLog;
import com.triple.mileage.dto.PointLogDTO;
import com.triple.mileage.exception.NoDataException;
import com.triple.mileage.repository.UserRepository;
import com.triple.mileage.domain.User;
import com.triple.mileage.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<PointLogDTO> findByPointLog(String userId) {
        Optional<User> dbUser =  userRepository.findByUserId(userId);
        if(dbUser.isPresent()){
            List<PointLog> pointLogList = dbUser.get().getPointLogs();
            List<PointLogDTO> logDTOList = new ArrayList<>();
            for (PointLog p : pointLogList) {
                logDTOList.add(new PointLogDTO(p.getLogId(),
                        p.getReviewId(),
                        p.getAmount(),
                        p.getAction(),
                        dbUser.get().getUserId()));
            }
            return logDTOList;
        }
        else{
            throw new NoDataException("No User have That Id");
        }
    }
}
