package org.example.base62.dao;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public interface UrlDao {
    Integer saveUrlInfo(NamedParameterJdbcTemplate shard, String longUrl, int ttl);
    String findLongUrlById(NamedParameterJdbcTemplate namedParameterJdbcTemplate,String id);
    String findLongUrl(String longUrl);
}
