package com.example.messengerapplication.controller;

import com.example.messengerapplication.model.User;
import com.example.messengerapplication.repository.UserRepository;
import com.example.messengerapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public User createUser(@Validated @RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/getUser/{email}")
    public User getUser(@PathVariable String email) {
          return userService.findByEmail(email);
    }
}
