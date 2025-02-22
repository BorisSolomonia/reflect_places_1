package com.boris.reflect_places_1.service;

import com.boris.reflect_places_1.entity.PlaceEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface PlaceServiceinterface {

    public List<PlaceEntity> getAllPlaces(@AuthenticationPrincipal Jwt jwt);
}
