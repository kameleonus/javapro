package com.example.javaProjektKc.controller;

import com.example.javaProjektKc.entity.User;
import com.example.javaProjektKc.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public Map<String, String> registerUser(@Valid @RequestBody User user) {
        Map<String, String> response = new HashMap<>();
        if (userRepository.findByEmail(user.getEmail()) != null) {
            response.put("message", "User with this email already exists!");
            return response;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        response.put("message", "User registered successfully!");
        return response;
    }
}
