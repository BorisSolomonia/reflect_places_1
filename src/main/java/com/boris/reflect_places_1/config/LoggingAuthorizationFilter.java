package com.boris.reflect_places_1.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class LoggingAuthorizationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAuthorizationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Retrieve the current authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            logger.info("User '{}' has authorities: {}", auth.getName(), auth.getAuthorities());
            // Optionally, check if "write:places" is among the authorities:
            boolean hasWritePlaces = auth.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("write:places"));
            logger.info("Has 'write:places' authority: {}", hasWritePlaces);
        } else {
            logger.info("No authentication found for this request.");
        }
        filterChain.doFilter(request, response);
    }
}
