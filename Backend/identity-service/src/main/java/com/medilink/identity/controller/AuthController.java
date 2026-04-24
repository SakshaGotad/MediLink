package com.medilink.identity.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class AuthController {
    @GetMapping("/login-success")
   public String loginSuccess(OAuth2AuthenticationToken authentication) {

    OAuth2User user = authentication.getPrincipal();

    String email = user.getAttribute("email");
    String name = user.getAttribute("name");

    return "Email: " + email + " Name: " + name;
}
}
