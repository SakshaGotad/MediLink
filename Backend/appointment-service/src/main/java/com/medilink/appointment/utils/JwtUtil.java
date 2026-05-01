package com.medilink.appointment.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JwtUtil {

    public UUID extractUserId(String token) {
        // TEMP: replace with real parsing later
        return UUID.randomUUID();
    }
}