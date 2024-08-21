package org.example.base62.dao;

public interface UrlDao {
    Integer saveUrlInfo(String longUrl, String shortUrl,int ttl);
    String findLongUrlByShortUrl(String shortUrl);
    String findLongUrl(String longUrl);
}
