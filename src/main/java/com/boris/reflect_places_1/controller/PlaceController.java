package com.boris.reflect_places_1.controller;

import com.boris.reflect_places_1.entity.PlaceEntity;
import com.boris.reflect_places_1.repo.PlaceRepository;
import com.boris.reflect_places_1.service.PlaceServiceinterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class PlaceController {

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private PlaceServiceinterface placeService;

    @PostMapping("/places")
    public PlaceEntity savePlace(@RequestBody PlaceEntity place, @AuthenticationPrincipal Jwt jwt) {
        System.out.println(jwt.toString());
        String username = jwt.getClaim("user-name"); // Extract the username from the JWT token
        place.setUsername(username); // Set the username in the place entity
        return placeRepository.save(place);
    }

    @GetMapping("/places")
    public List<PlaceEntity> getAllPlaces(@AuthenticationPrincipal Jwt jwt) {
        return placeService.getAllPlaces(jwt);
    }

    @GetMapping("/demo")
    public String demo() {
        return "Hello World!";
    }


}
