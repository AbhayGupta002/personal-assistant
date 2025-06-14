package com.personal.assistant.controller;

import com.personal.assistant.dto.ErrorDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.personal.assistant.dto.UserDto;
import com.personal.assistant.entity.UserDetails;
import com.personal.assistant.entity.UserLogin;
import com.personal.assistant.repository.UserDetailsRepository;
import com.personal.assistant.repository.UserLoginRepository;
import java.util.UUID;

@RestController
@RequestMapping("login")
public class LoginController {

    @Autowired
    private UserDetailsRepository userRepo;

    @Autowired
    private UserLoginRepository loginRepo;

    @PostMapping(value = "register")
    public UserDto createUser(@RequestBody UserDto user){
        UserDetails userDetails = new UserDetails();
        String userId = UUID.randomUUID().toString();
        userDetails.setId(userId);
        userDetails.setName(user.getName());
        userDetails.setAddress(user.getAddress());
        userDetails.setContactNumber(user.getContactNumber());
        userDetails.setEmail(user.getEmail());
        userDetails.setStatus("Active");
        try{
            userRepo.save(userDetails);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        UserLogin login = new UserLogin();
        login.setEmail(user.getEmail());
        login.setPassword(user.getPassword());
        try{
            loginRepo.save(login);
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Email id or password not saved");
        }

        UserDto dto = new UserDto();
        dto.setId(userDetails.getId());
        dto.setName(userDetails.getName());
        dto.setAddress(userDetails.getAddress());
        dto.setEmail(userDetails.getEmail());
        dto.setContactNumber(userDetails.getContactNumber());

        return dto;
    }

    @GetMapping(value = "/")
    public UserDto getUser (@RequestBody UserDto userDto) {
        UserDto user = new UserDto();
        UserDetails userDetails = userRepo.findById(userDto.getId()).orElseThrow(() -> new RuntimeException("User not found with id " + userDto.getId()));

        user.setId(userDetails.getId());
        user.setName(userDetails.getName());
        user.setAddress(userDetails.getAddress());
        user.setContactNumber(userDetails.getContactNumber());
        user.setEmail(userDetails.getEmail());
        return user;
    }

    @PostMapping("authenticate")
    public String userLogin(@RequestBody UserLogin login) {
        int retVal = loginRepo.findByEmailAndPassword(login.getEmail(), login.getPassword());
        if (retVal == 1) {
            return "Login successful";
        }
        return "Invalid ID or Password";
    }
}
