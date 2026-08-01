package org.solen.configuration.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey key;

    private static final long EXPIRATION_MS = 7200000;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        if (secret.getBytes().length < 32) {
            throw new IllegalArgumentException("JWT secret must be at least 256 bits (32 bytes)");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String email, Long userId, String name) {
            return Jwts.builder()
                    .setSubject(email)
                    .claim("userId", userId)
                    .claim("name", name)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();
    }

    // Parses the token and returns the subject (email). Used by JwtAuthFilter
    // to identify the user and look up their ID from the DB.
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Returns true if the token is structurally valid and not expired.
    // Does NOT verify that the user still exists in the DB (the filter handles that).
    public boolean validateToken(String token) {
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }
}
