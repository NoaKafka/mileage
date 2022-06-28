package com.triple.mileage.controller;

import com.triple.mileage.domain.User;
import com.triple.mileage.repository.query.UserDTO;
import com.triple.mileage.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/point")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDTO getUserPoint(@RequestBody UserDTO user){
        return userService.findByUserId(user);
    }
}
