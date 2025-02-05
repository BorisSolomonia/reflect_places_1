package com.boris.reflect_places_1.service;

import com.boris.reflect_places_1.entity.PlaceEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public interface PlaceService {

    public List<PlaceEntity> getAllPlaces(@AuthenticationPrincipal Jwt jwt);

    public PlaceEntity save(PlaceEntity place);
    public List<PlaceEntity> findByUsername(Jwt jwt);
}
