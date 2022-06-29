package com.triple.mileage.controller;

import com.triple.mileage.dto.UserDTO;
import com.triple.mileage.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/point")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserPoint(@PathVariable String userId){
        UserDTO userDTO = userService.findByUserId(userId);
        return ResponseEntity.ok(userDTO) ;
    }
}
