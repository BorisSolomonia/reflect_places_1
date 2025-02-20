package com.boris.reflect_places_1.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

public class TokenLoggingFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(TokenLoggingFilter.class);
    private final JwtDecoder jwtDecoder;

    public TokenLoggingFilter(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Jwt jwt = jwtDecoder.decode(token);
                logger.info("✅ Valid Token Received: {}", jwt.getClaims());
                logger.info("👤 Boris Scope: {}", Optional.ofNullable(jwt.getClaim("scope")));

                // Log authorities after conversion
                JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
                JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
                authoritiesConverter.setAuthoritiesClaimName("scope");
                authoritiesConverter.setAuthorityPrefix("");
                converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
                Authentication auth = converter.convert(jwt);
                logger.info("🔑 Converted Authorities: {}", auth.getAuthorities());
            } catch (Exception e) {
                logger.error("🚨 Invalid Token: {}", e.getMessage());
            }
        } else {
            logger.warn("⚠️ No Bearer Token found in request!");
        }

        filterChain.doFilter(request, response);

        // Log SecurityContext after the chain
        Authentication finalAuth = SecurityContextHolder.getContext().getAuthentication();
        logger.info("🔍 Final SecurityContext after chain: {}", finalAuth);
    }
}
