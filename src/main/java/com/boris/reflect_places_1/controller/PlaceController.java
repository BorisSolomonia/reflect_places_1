package com.boris.reflect_places_1.controller;
import com.boris.reflect_places_1.entity.Place;
import com.boris.reflect_places_1.entity.PlaceEntity;
import com.boris.reflect_places_1.repo.PlaceRepository;
import com.boris.reflect_places_1.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PlaceController {

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private PlaceService placeService;

    @GetMapping("/places")
    public List<PlaceEntity> getPlaces(@AuthenticationPrincipal Jwt jwt) {

        return placeService.findByUsername(jwt);
    }

    @PostMapping("/places")
    public ResponseEntity<Place> createPlace(@RequestBody PlaceEntity place,
                                             @AuthenticationPrincipal Jwt jwt) {
        place.setUsername(jwt.getClaim("name"));
        place.setCreatedAt(LocalDateTime.now());
        Place savedPlace = placeService.save(place);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPlace);
    }
}
