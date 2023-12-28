package com.yprojects.urlShortener.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "long_url", nullable = false, length = 2048)
    private String longUrl;

    @Column(name = "short_url", nullable = false, unique = true)
    private String shortUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Url(String longUrl, String shortUrl, LocalDateTime createdAt) {
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
        this.createdAt = createdAt;
    }
}
