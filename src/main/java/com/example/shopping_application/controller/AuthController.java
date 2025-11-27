package com.example.shopping_application.controller;

import com.example.shopping_application.dto.LoginRequest;
import com.example.shopping_application.entity.User;
import com.example.shopping_application.service.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/users")
@RequiredArgsConstructor
public class AuthController {

    private final UserRegistrationService userRegistrationService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user){
        User newUser=userRegistrationService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody LoginRequest loginRequest){
         userRegistrationService.loginUser(loginRequest);
         return "Logged in successfully";
    }

}
