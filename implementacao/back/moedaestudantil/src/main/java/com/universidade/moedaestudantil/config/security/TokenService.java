package com.universidade.moedaestudantil.config.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import com.universidade.moedaestudantil.model.Usuario;

@Service
public class TokenService {

    @Value("${api.security.jwt.secret}")
    private String jwtSecret;

    public String generateToken(Usuario user) {
        try {

            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);

            String token = JWT.create()
                    .withIssuer("login-auth-api") // api que emite o token
                    .withSubject(user.getEmail())
                    .withExpiresAt(this.generateExpirationDate())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error generating token", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);

            return JWT.require(algorithm)
                    .withIssuer("login-auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2)
                .toInstant(ZoneOffset.of("-03:00"));
    }

    public String getUserEmailFromToken(String token) {
        System.out.println("Decoding token to get user ID: " + token);
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);

            String subject = JWT.require(algorithm)
                    .withIssuer("login-auth-api")
                    .build()
                    .verify(token)
                    .getSubject();

            return subject;
        } catch (JWTVerificationException exception) {
            return null;
        }
    }
}