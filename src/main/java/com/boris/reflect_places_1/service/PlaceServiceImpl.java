package com.boris.reflect_places_1.service;

import com.boris.reflect_places_1.entity.PlaceEntity;
import com.boris.reflect_places_1.repo.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceServiceImpl implements PlaceService {

    @Autowired
    private PlaceRepository placeRepository;


    @Override
    public List<PlaceEntity> getAllPlaces(Jwt jwt) {
        String username = jwt.getClaim("user-name"); // Extract the username from the JWT token
        System.out.println(username);
        return placeRepository.findByUsername(username);
    }
}
