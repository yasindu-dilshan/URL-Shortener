package com.yprojects.urlShortener.controller;

import com.yprojects.urlShortener.entity.Url;
import com.yprojects.urlShortener.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/url")
public class UrlController {

    @Autowired
    private UrlService urlService;

    @PostMapping("/shorten")
    public ResponseEntity<Url> shortenUrl(@RequestParam String longUrl) {
        Url shortenedUrl = urlService.shortenUrl(longUrl);
        return new ResponseEntity<>(shortenedUrl, HttpStatus.CREATED);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<String> getLongUrl(@PathVariable String shortUrl) {
        Optional<Url> url = urlService.findUrlByShortUrl(shortUrl);
        if (url.isPresent()) {
            return ResponseEntity.ok(url.get().getLongUrl());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
