package com.medilink.identity.security;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.medilink.identity.service.UserService;
import com.medilink.identity.entity.User;

import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OauthSuccesshandler extends SimpleUrlAuthenticationSuccessHandler {
private final UserService userService;
private final JwtService jwtService;
  public OauthSuccesshandler(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }
    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException, ServletException{
         OAuth2User user = (OAuth2User) authentication.getPrincipal();

        String email = user.getAttribute("email");
        String name = user.getAttribute("name");

       User savedUser = userService.findOrCreateUser(email, name);
        String token = jwtService.generateToken(savedUser);
         System.out.println("User saved: " + savedUser.getEmail());

        // 🔥 TEMP: just redirect
        response.sendRedirect("http://localhost:3000?email=" + email);
    }
}
