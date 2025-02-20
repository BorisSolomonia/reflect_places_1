package com.boris.reflect_places_1.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "places")
public class PlaceEntity implements Place{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double latitude;
    private Double longitude;
    private String description;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "user_name")
    private String username;

    // Getters and Setters

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    public PlaceEntity(String name, double lat, double lng, String username) {
        this.name = name;
        this.latitude = lat;
        this.longitude = lng;
        this.username = username;
    }
}
