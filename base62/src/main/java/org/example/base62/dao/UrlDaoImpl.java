package org.example.base62.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UrlDaoImpl implements UrlDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer saveUrlInfo(String longUrl, String shortUrl, int ttl) {
        String sql = "INSERT INTO info (long_url, short_url,ttl) VALUES (:longUrl, :shortUrl , :ttl)";
        Map<String, Object> map = new HashMap<>();
        map.put("longUrl", longUrl);
        map.put("shortUrl", shortUrl);
        map.put("ttl", ttl);
        try {
            namedParameterJdbcTemplate.update(sql, map);
            return 1;
        } catch (DataIntegrityViolationException e){
            return 0;
        } catch (DataAccessException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public List<String> findLongUrl(String longUrl) {
        String sql = "SELECT short_url FROM info WHERE long_url=:longUrl";
        Map<String, Object> map = new HashMap<>();
        map.put("longUrl", longUrl);
        try {
            return namedParameterJdbcTemplate.query(sql, map, (rs, rowNum) -> rs.getString("short_url"));
        } catch (EmptyResultDataAccessException e ) {
            return null;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String findLongUrlByShortUrl(String shortUrl) {
        String sql = "SELECT long_url FROM info WHERE short_url=:shortUrl";
        Map<String, Object> map = new HashMap<>();
        map.put("shortUrl", shortUrl);
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, map, String.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
