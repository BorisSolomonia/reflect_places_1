package com.boris.reflect_places_1.controller;

import com.boris.reflect_places_1.entity.UserProfile;
import com.boris.reflect_places_1.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProfileController {
    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfile> getProfile(Authentication auth) {
        String userId = auth.getName(); // Auth0 sub claim
        UserProfile profile = profileService.getProfile(userId);
        return ResponseEntity.ok(profile);
    }

    @PatchMapping("/profile")
    public ResponseEntity<UserProfile> updateProfile(@RequestBody UserProfile profileData, Authentication auth) {
        String userId = auth.getName();
        UserProfile updated = profileService.updateProfile(userId, profileData);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/upload/profile-pic")
    public ResponseEntity<Map<String, String>> uploadProfilePic(@RequestParam("file") MultipartFile file, Authentication auth) {
        try {
            String userId = auth.getName();
            String url = profileService.uploadProfilePic(userId, file);
            return ResponseEntity.ok(Map.of("url", url));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to upload profile picture"));
        }
    }

    @PostMapping("/upload/cover-photo")
    public ResponseEntity<Map<String, String>> uploadCoverPhoto(@RequestParam("file") MultipartFile file, Authentication auth) {
        try {
            String userId = auth.getName();
            String url = profileService.uploadCoverPhoto(userId, file);
            return ResponseEntity.ok(Map.of("url", url));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to upload cover photo"));
        }
    }
}