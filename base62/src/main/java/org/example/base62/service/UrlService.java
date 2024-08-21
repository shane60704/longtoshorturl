package org.example.base62.service;

import org.example.base62.dto.shortenRequest;

public interface UrlService {
    String shortenUrl(shortenRequest shortenRequest);
    String getLongUrl(String shortUrl);
}
