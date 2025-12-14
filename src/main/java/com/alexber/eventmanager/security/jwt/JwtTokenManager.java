package com.alexber.eventmanager.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenManager {

    private final SecretKey secretKey;
    private final Long tokenValidityInSeconds;

    public JwtTokenManager(@Value("${jwt.secret-key}") String  secretKey, @Value("${jwt.lifetime}") Long tokenValidityInSeconds) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.tokenValidityInSeconds = tokenValidityInSeconds;
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
