package com.boris.reflect_places_1.repo;

import com.boris.reflect_places_1.entity.Place;
import com.boris.reflect_places_1.entity.PlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceRepository extends JpaRepository<PlaceEntity, Long> {
    public List<PlaceEntity> findByUsername(String username);
}
