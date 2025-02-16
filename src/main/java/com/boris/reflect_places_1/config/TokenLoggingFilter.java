package com.boris.reflect_places_1.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TokenLoggingFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(TokenLoggingFilter.class);

    private final JwtDecoder jwtDecoder;

    public TokenLoggingFilter(String issuerUri) {
        this.jwtDecoder = JwtDecoders.fromIssuerLocation(issuerUri); // ✅ Decode token
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            logger.info("🔑 Received JWT Token (first 50 chars): {}", token.substring(0, Math.min(50, token.length())));

            try {
                Jwt decodedJwt = jwtDecoder.decode(token);
                logger.info("🔍 Token Payload: {}", decodedJwt.getClaims());  // ✅ Log full claims
            } catch (Exception e) {
                logger.error("❌ Failed to decode JWT: {}", e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}




//package com.boris.reflect_places_1.config;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//public class TokenLoggingFilter extends OncePerRequestFilter {
//    private static final Logger logger = LoggerFactory.getLogger(TokenLoggingFilter.class);
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7);
//            logger.debug("Received JWT Token (first 20 chars): {}", token.substring(0, Math.min(20, token.length())));
//        }
//        filterChain.doFilter(request, response);
//    }
//}
