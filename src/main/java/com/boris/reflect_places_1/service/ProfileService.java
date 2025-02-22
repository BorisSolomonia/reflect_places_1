package com.boris.reflect_places_1.service;


import com.boris.reflect_places_1.entity.UserProfile;
import com.boris.reflect_places_1.repo.UserProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProfileService {
    private static final Logger logger = LoggerFactory.getLogger(ProfileService.class);
    private final UserProfileRepository userProfileRepository;

    @Value("${file.upload-dir:/uploads}")
    private String uploadDir; // Configure in application.properties (e.g., file.upload-dir=/path/to/uploads)

    public ProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public UserProfile getProfile(String userId) {
        Optional<UserProfile> profileOpt = userProfileRepository.findById(userId);
        if (profileOpt.isEmpty()) {
            UserProfile newProfile = new UserProfile();
            newProfile.setUserId(userId);
            newProfile.setNickname(userId); // Default to userId if no profile exists
            return userProfileRepository.save(newProfile);
        }
        return profileOpt.get();
    }

    public UserProfile updateProfile(String userId, UserProfile profileData) {
        UserProfile profile = getProfile(userId);
        profile.setNickname(profileData.getNickname() != null ? profileData.getNickname() : profile.getNickname());
        profile.setBio(profileData.getBio() != null ? profileData.getBio() : profile.getBio());
        profile.setLocation(profileData.getLocation() != null ? profileData.getLocation() : profile.getLocation());
        profile.setWebsite(profileData.getWebsite() != null ? profileData.getWebsite() : profile.getWebsite());
        profile.setProfilePicture(profileData.getProfilePicture() != null ? profileData.getProfilePicture() : profile.getProfilePicture());
        profile.setCoverPhoto(profileData.getCoverPhoto() != null ? profileData.getCoverPhoto() : profile.getCoverPhoto());
        return userProfileRepository.save(profile);
    }

    public String uploadProfilePic(String userId, MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, "profile-pics", userId, fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());
        String url = String.format("https://www.brooks-dusura.uk/uploads/profile-pics/%s/%s", userId, fileName);
        logger.info("Uploaded profile picture for user {}: {}", userId, url);
        return url;
    }

    public String uploadCoverPhoto(String userId, MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, "cover-photos", userId, fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());
        String url = String.format("https://www.brooks-dusura.uk/uploads/cover-photos/%s/%s", userId, fileName);
        logger.info("Uploaded cover photo for user {}: {}", userId, url);
        return url;
    }
}