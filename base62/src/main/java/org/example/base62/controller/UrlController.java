package org.example.base62.controller;

import jakarta.validation.Valid;
import org.example.base62.dto.shortenRequest;
import org.example.base62.service.UrlServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;


import java.net.URI;
import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<?> shortenUrl(@Valid @RequestBody shortenRequest shortenRequest) {
        if (urlServiceImpl.shortenUrl(shortenRequest) != null) {
            return ResponseEntity.ok(urlServiceImpl.shortenUrl(shortenRequest));
        }else{
            return ResponseEntity.badRequest().body("failed to shorten");
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // 逐個字段處理錯誤信息
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }



}
