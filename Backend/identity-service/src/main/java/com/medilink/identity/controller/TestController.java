package com.medilink.identity.controller;

import com.medilink.identity.entity.*;
import com.medilink.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final UserRepository userRepository;

    @GetMapping("/db")
    public String testDb() {

        User user = User.builder()
                .email("test@gmail.com")
                .name("Test User")
                .role(Role.PATIENT)
                .provider(AuthProvider.GOOGLE)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        return "User saved successfully!";
    }

     @GetMapping("/api/test")
    public String test(Authentication authentication) {
        return "Hello " + authentication.getName();
    }
}