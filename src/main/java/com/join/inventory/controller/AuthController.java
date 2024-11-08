package com.join.inventory.controller;

import com.join.inventory.model.User;
import com.join.inventory.model.dto.LoginRequest;
import com.join.inventory.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.status(HttpStatus.OK).body("Login successful");
    }

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody User newUser) {
        userRepository.save(newUser);
        return ResponseEntity.ok("User created successfully");
    }
}