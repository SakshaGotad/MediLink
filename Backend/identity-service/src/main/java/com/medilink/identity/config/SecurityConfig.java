package com.medilink.identity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.medilink.identity.security.OauthSuccesshandler;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
@Configuration
public class SecurityConfig {

    private final OauthSuccesshandler successHandler;

    public SecurityConfig(OauthSuccesshandler successHandler) {
        this.successHandler = successHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/test/**").permitAll() // allow test endpoint
                .anyRequest().authenticated()
            )
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable()) 
            .oauth2Login(oauth -> oauth
                .successHandler(successHandler)
            );

        return http.build();
    }
}