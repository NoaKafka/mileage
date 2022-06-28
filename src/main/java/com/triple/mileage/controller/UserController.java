package com.triple.mileage.controller;

import com.triple.mileage.domain.User;
import com.triple.mileage.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/point")
    public User getUserPoint(@RequestBody String userId){
        return userService.findByUserId(userId);
    }
}
