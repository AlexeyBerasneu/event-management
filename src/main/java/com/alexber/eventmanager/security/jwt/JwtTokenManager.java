package com.alexber.eventmanager.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenManager {

    @Value("${jwt.secret-key}")
    private String key;

    @Value("${jwt.lifetime}")
    private Long tokenValidityInSeconds;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(key.getBytes());
    }

    public String generateToken(String login) {
        return Jwts
                .builder()
                .setSubject(login)
                .signWith(secretKey)
                .setIssuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenValidityInSeconds))
                .compact();
    }

    public String getLoginFromToken(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
