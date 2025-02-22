package com.boris.reflect_places_1.repo;

import com.boris.reflect_places_1.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
}
