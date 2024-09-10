package com.boris.reflect_places_1.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/test")
    public String testCors(HttpServletRequest request, HttpServletResponse response) {
        // Log headers before sending the response
        System.out.println("Controller CORS headers:");
        System.out.println("Access-Control-Allow-Origin: " + response.getHeader("Access-Control-Allow-Origin"));
        return "CORS headers logged";
    }
}