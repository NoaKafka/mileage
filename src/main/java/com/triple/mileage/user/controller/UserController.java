package com.triple.mileage.user.controller;

import com.triple.mileage.user.data.entity.User;
import com.triple.mileage.user.service.UserService;
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