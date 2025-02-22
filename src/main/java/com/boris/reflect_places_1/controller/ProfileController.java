package com.boris.reflect_places_1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfile> getProfile(Authentication auth) {
        UserProfile profile = profileService.getProfile(auth.getName());
        return ResponseEntity.ok(profile);
    }

    @PatchMapping("/profile")
    public ResponseEntity<UserProfile> updateProfile(@RequestBody UserProfile profile, Authentication auth) {
        UserProfile updated = profileService.updateProfile(auth.getName(), profile);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/upload/profile-pic")
    public ResponseEntity<Map<String, String>> uploadProfilePic(@RequestParam("file") MultipartFile file, Authentication auth) {
        String url = profileService.uploadProfilePic(auth.getName(), file);
        return ResponseEntity.ok(Map.of("url", url));
    }

    @PostMapping("/upload/cover-photo")
    public ResponseEntity<Map<String, String>> uploadCoverPhoto(@RequestParam("file") MultipartFile file, Authentication auth) {
        String url = profileService.uploadCoverPhoto(auth.getName(), file);
        return ResponseEntity.ok(Map.of("url", url));
    }
}