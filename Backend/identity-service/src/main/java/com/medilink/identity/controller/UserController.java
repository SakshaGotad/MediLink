package com.medilink.identity.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medilink.identity.entity.User;
import com.medilink.identity.security.JwtService;
import com.medilink.identity.service.UserService;

@RestController
@RequestMapping("api/users")
public class UserController {
     private final UserService userService;
    private final JwtService jwtService;

     public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

     @PutMapping("/role")
      public String updateRole(
            @RequestParam String role,
            Authentication authentication
    ) {

        String email = authentication.getName();

        User updatedUser = userService.updateRole(email, role);

        // 🔥 Generate NEW JWT with updated role
        String newToken = jwtService.generateToken(updatedUser);

        return newToken;
    } 
}
