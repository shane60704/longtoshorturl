package org.example.base62.controller;

import org.example.base62.dto.shortenRequest;
import org.example.base62.service.UrlServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.net.URI;

@Controller
public class UrlController {

    private static final Logger log = LoggerFactory.getLogger(UrlController.class);
    @Autowired
    private UrlServiceImpl urlServiceImpl;

    @GetMapping("/")
    public String showIndex() {
        return "index";
    }

    @GetMapping("/index.html")
    public String index() {
        return "index";
    }

    @GetMapping("/{shortKey}")
    public ResponseEntity<Void> redirectToLongUrl(@PathVariable String shortKey) {

        String longUrl = urlServiceImpl.getLongUrl(shortKey);
        if (longUrl != null) {
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(longUrl)).build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/api/shorten")
    @ResponseBody
    public String shortenUrl(@RequestBody shortenRequest shortenRequest) {
        return urlServiceImpl.shortenUrl(shortenRequest);
    }



}
