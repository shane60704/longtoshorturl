package org.example.base62.dao;

import java.util.List;

public interface UrlDao {
    Integer saveUrlInfo(String longUrl, String shortUrl,int ttl);
    String findLongUrlByShortUrl(String shortUrl);
    List<String> findLongUrl(String longUrl);
}
