package com.techie.spring.security.controller;

import com.techie.spring.security.entity.User;
import com.techie.spring.security.repository.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user){
        return userRepository.save(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user){
        var u = userRepository.findByUsername(user.getUsername());
        if(!Objects.isNull(u))
            return "success";
        else
            return "failure";
    }
}
