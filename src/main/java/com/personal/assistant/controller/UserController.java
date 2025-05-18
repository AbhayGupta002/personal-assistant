package com.personal.assistant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.personal.assistant.entity.UserDetails;
import com.personal.assistant.repository.UserDetailsRepository;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserDetailsRepository userRepo;

    @PostMapping
    public UserDetails getUser(@RequestBody String userId){
        return userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

}