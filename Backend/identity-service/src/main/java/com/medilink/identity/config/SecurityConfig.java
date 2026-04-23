package com.medilink.identity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.medilink.identity.security.JwtFilter;
import com.medilink.identity.security.OauthSuccesshandler;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
@Configuration
public class SecurityConfig {

    private final OauthSuccesshandler successHandler;
    private final JwtFilter jwtFilter;
    public SecurityConfig(OauthSuccesshandler successHandler, JwtFilter jwtFilter) {
        this.successHandler = successHandler;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/oauth2/**").permitAll() // allow test endpoint
                .anyRequest().authenticated()
            )
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable()) 
            .oauth2Login(oauth -> oauth
                .successHandler(successHandler)
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}