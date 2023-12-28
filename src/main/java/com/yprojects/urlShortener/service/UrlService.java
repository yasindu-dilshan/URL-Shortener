package com.yprojects.urlShortener.service;

import com.yprojects.urlShortener.entity.Url;
import com.yprojects.urlShortener.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    public Url shortenUrl(String longUrl) {
        Optional<Url> existingUrl = urlRepository.findByLongUrl(longUrl);
        if (existingUrl.isPresent()) {
            return existingUrl.get();
        }

        String shortUrl = generateShortUrl(longUrl);

        Url url = new Url(longUrl, shortUrl, LocalDateTime.now());
        return urlRepository.save(url);
    }

    public Optional<Url> findUrlByShortUrl(String shortUrl) {
        return urlRepository.findByShortUrl(shortUrl);
    }

    private String generateShortUrl(String longUrl) {
        try {
            // Generate a unique identifier
            String uniqueId = UUID.randomUUID().toString();

            // Concatenate the current timestamp, unique identifier, and the long URL
            String combinedString = LocalDateTime.now().toString() + uniqueId + longUrl;

            // Apply the SHA-256 hash function to the combined string
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(combinedString.getBytes(StandardCharsets.UTF_8));

            // Convert the hash bytes to a Base64-encoded representation
            String base64Hash = Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes);

            // Take the first 8 characters as the short URL
            String shortUrl = base64Hash.substring(0, 8);

            // Check for uniqueness and handle collisions
            int attempt = 1;
            while (urlRepository.findByShortUrl(shortUrl).isPresent()) {
                // If a collision is detected, modify the short URL and try again
                shortUrl = base64Hash.substring(attempt, attempt + 8);
                attempt++;

                // Add a safeguard to avoid an infinite loop (e.g., in case of extremely unlikely collisions)
                if (attempt > 100) {
                    throw new RuntimeException("Unable to generate a unique short URL");
                }
            }

            return shortUrl;

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating short URL", e);
        }

    }
}
