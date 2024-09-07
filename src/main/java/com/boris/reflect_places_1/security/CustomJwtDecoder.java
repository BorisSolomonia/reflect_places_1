package com.boris.reflect_places_1.security;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

public class CustomJwtDecoder implements JwtDecoder {

    private final JwtDecoder delegate;

    public CustomJwtDecoder(String jwkSetUri) {
        this.delegate = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        System.out.println("Attempting to decode JWT...");
        System.out.println("JWT Token: " + token);
        try {
            Jwt jwt = delegate.decode(token);
            System.out.println("JWT validated successfully: " + jwt);
            return jwt;
        } catch (JwtException e) {
            System.err.println("JWT validation failed: yo " + e.getMessage());
            throw e;
        }
    }
}
