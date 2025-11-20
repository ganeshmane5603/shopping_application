package com.example.shopping_application.service;

import com.example.shopping_application.dto.LoginRequest;
import com.example.shopping_application.entity.User;
import com.example.shopping_application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final UserRepository userRepository;

    public User registerUser(User user){
        if (user.getEmail()==null || user.getEmail().isEmpty()){
            throw new IllegalArgumentException("Email is empty");
        }

        if (userRepository.existsByEmail(user.getEmail())){
            throw new DataIntegrityViolationException("Email already exists");
        }

        if (user.getPassword() == null || user.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
        return userRepository.save(user);
    }

    public void loginUser(LoginRequest loginRequest){
           User user=userRepository.findByEmail(loginRequest.getEmail());
              if ((user==null) || user.getEmail().isEmpty()){
                  throw new IllegalArgumentException("Enter valid email");
        }

           if (!user.getPassword().equals(loginRequest.getPassword())){
               throw new IllegalArgumentException("Password not match");
           }

        }

    }

