package com.boris.reflect_places_1.config;

import com.boris.reflect_places_1.security.CustomJwtDecoder;
import com.boris.reflect_places_1.security.LoggingFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Define the security filter chain bean ჩამქვრალია 10/09/2024
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // Disable CSRF protection for stateless sessions
                .csrf(AbstractHttpConfigurer::disable)
                // Configure URL authorization rules
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/places").authenticated() // Require authentication for /api/places
                                .anyRequest().permitAll() // Allow all other requests
                )
                // Set session management to stateless (no session will be created or used)
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // Configure OAuth2 resource server to use custom authentication manager resolver
                .oauth2ResourceServer(oauth ->
                        oauth.authenticationManagerResolver(authManagerResolver())
                )
                // Configure CORS with custom source
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));


        // Add a custom logging filter before the UsernamePasswordAuthenticationFilter
        httpSecurity.addFilterBefore(new LoggingFilter(), UsernamePasswordAuthenticationFilter.class);

        // Return the configured SecurityFilterChain
        return httpSecurity.build();
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(authorizeRequests ->
//                        authorizeRequests
//                                .requestMatchers("/api/places").authenticated()
//                                .anyRequest().permitAll()
//                )
//                .sessionManagement(sm ->
//                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
//                .oauth2ResourceServer(oauth ->
//                        oauth.authenticationManagerResolver(authManagerResolver())
//                )
//                .cors(cors -> cors.configurationSource(corsConfigurationSource())); // Enable CORS
//
//        httpSecurity.addFilterBefore(new LoggingFilter(), UsernamePasswordAuthenticationFilter.class);
//
//        return httpSecurity.build();
//    }

    @Value("${AUTH_SERVER:http://localhost:8080}")
    private String authServerUrl;

    @Value("${AUTH_SERVER_JWKS:http://localhost:8080/oauth2/jwks}")
    private String authServerJwksUrl;

    // Define a bean for JwtIssuerAuthenticationManagerResolver
    @Bean
    public JwtIssuerAuthenticationManagerResolver authManagerResolver() {
        Map<String, AuthenticationManager> map = new HashMap<>();
        // Map the issuer URL to the corresponding authentication manager
        map.put(authServerUrl, authenticationManager(authServerJwksUrl));
        // Return a resolver that uses the map to resolve authentication managers based on the issuer
        return new JwtIssuerAuthenticationManagerResolver(map::get);
    }

    // Create an AuthenticationManager for a given JWK Set URI
    private AuthenticationManager authenticationManager(String jwkSetUri) {
        // Print the JWK Set URI to the console for debugging
        System.out.println("Fetching JWK Set from URI: " + jwkSetUri);
        // Create a custom JwtDecoder with the given JWK Set URI
        JwtDecoder decoder = new CustomJwtDecoder(jwkSetUri);
        // Create a JwtAuthenticationProvider with the custom JwtDecoder
        JwtAuthenticationProvider provider = new JwtAuthenticationProvider(decoder);
        // Print a message to the console for debugging
        System.out.println("Created JwtAuthenticationProvider with custom JwtDecoder");
        // Return an AuthenticationManager with the JwtAuthenticationProvider
        return new ProviderManager(provider);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("https://www.brooks-dusura.uk","https://api.brooks-dusura.uk")); // Add allowed origins here
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE")); // Allowed HTTP methods
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type")); // Allowed headers
        configuration.setAllowCredentials(true); // Allow credentials (if needed)
        configuration.setExposedHeaders(List.of("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
