package com.medilink.identity.service;

import com.medilink.identity.entity.User;
import org.springframework.stereotype.Service;

import com.medilink.identity.entity.AuthProvider;
import com.medilink.identity.entity.Role;
import com.medilink.identity.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findOrCreateUser(String email, String name){
        return userRepository.findByEmail(email)
        .orElseGet(()->{
            User newUser = User.builder()
            .email(email)
            .name(name)
            .role(Role.PATIENT) 
            .provider(AuthProvider.GOOGLE)
            .build();
            return userRepository.save(newUser);
        });
                
    }
}
