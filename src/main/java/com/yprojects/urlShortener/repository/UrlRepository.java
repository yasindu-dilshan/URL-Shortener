package com.yprojects.urlShortener.repository;

import com.yprojects.urlShortener.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByLongUrl(String longUrl);
    Optional<Url> findByShortUrl(String shortUrl);
}
