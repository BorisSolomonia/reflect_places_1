package com.boris.reflect_places_1.config;

import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.ParseException;

public class TokenLoggingFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(TokenLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                SignedJWT signedJWT = SignedJWT.parse(token);

                logger.info("🔑 Received JWT Token: {}", token.substring(0, Math.min(50, token.length())) + "...");
                logger.info("🔍 Token Issuer: {}", signedJWT.getJWTClaimsSet().getIssuer());
                logger.info("🔍 Token Audience: {}", signedJWT.getJWTClaimsSet().getAudience());
                logger.info("🔍 Token Scope: {}", signedJWT.getJWTClaimsSet().getStringClaim("scope"));

            } catch (ParseException e) {
                logger.error("🚨 Failed to parse JWT", e);
            }
        } else {
            logger.warn("🚨 No Authorization header found!");
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
