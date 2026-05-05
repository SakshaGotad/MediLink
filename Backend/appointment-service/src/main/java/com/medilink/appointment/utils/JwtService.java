package com.medilink.appointment.utils;
 
import java.security.Key;
import java.util.Date;
import java.util.UUID;
 
import org.springframework.stereotype.Service;
 
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
 
@Service
public class JwtService {
    private final String SECRET = "my-super-secret-key-that-is-at-least-32-characters-long";
 
    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }
 
    public String generateToken(UUID userId, String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }
 
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
 
    public UUID extractUserId(String token) {
        Object userId = Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userId");
        
        if (userId == null) return null;
        return UUID.fromString(userId.toString());
    }

    public String extractRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }
 
    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
