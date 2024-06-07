package com.microservices.auth.utils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.sentry.Sentry;
import io.sentry.SentryLevel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.logging.Logger;

@Component
public class Jwt {
    @Value("${jwt.secret}")
    private String secret;

    private static final Logger logger = Logger.getLogger(Jwt.class.getName());

    public String generateToken(String userId) {
        try {
            SecretKey key = getSigningKey(secret);
            return Jwts.builder().subject(userId).signWith(key).compact();

        } catch (JwtException e) {
            Sentry.captureMessage("Error occurred while generating JWT token: " + e.getMessage(), SentryLevel.FATAL);
            logger.severe("Error occurred while generating JWT token: " + e.getMessage());
            return null;
        }
    }

    public String validateToken(String token) {
        try {
            SecretKey key = getSigningKey(secret);

            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (JwtException e) {
            Sentry.captureMessage("Error occurred while validating JWT token: " + e.getMessage(), SentryLevel.FATAL);
            logger.severe("Error occurred while validating JWT token: " + e.getMessage());
            return null;
        }
    }

    public static SecretKey getSigningKey(String secret) {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secret);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            Sentry.captureMessage("Error occurred while getting secret key: " + e.getMessage(), SentryLevel.FATAL);
            logger.severe("Error occurred while getting secret key: " + e.getMessage());
            return null;
        }
    }
}
