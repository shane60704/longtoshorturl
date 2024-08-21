package org.example.base62.dao;

public interface UrlDao {
    Integer saveUrlInfo(String longUrl,int ttl);
    String findLongUrlById(String id);
    String findLongUrl(String longUrl);
}
