package com.example.shopping_application.controller;

import com.example.shopping_application.dto.LoginRequest;
import com.example.shopping_application.dto.LoginResponse;
import com.example.shopping_application.entity.User;
import com.example.shopping_application.security.JwtUtils;
import com.example.shopping_application.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/users")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user){
        User newUser=userDetailsServiceImpl.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            // 2. Load userDetails
            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(request.getEmail());
            // 3. Generate token
            String token = jwtUtils.generateToken(userDetails);
            // 4. Return DTO
            return ResponseEntity.ok(new LoginResponse(token, "Bearer"));
        }
        catch (BadCredentialsException e) {
            return ResponseEntity.status(401)
                    .body(new LoginResponse("Invalid credentials", "Error"));
        }
    }
}
