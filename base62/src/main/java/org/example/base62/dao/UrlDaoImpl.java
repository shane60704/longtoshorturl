package org.example.base62.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UrlDaoImpl implements UrlDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

//    @Override
//    public Integer saveUrlInfo(String longUrl, String shortUrl, int ttl) {
//        String sql = "INSERT INTO info (long_url, short_url,ttl) VALUES (:longUrl, :shortUrl , :ttl)";
//        Map<String, Object> map = new HashMap<>();
//        map.put("longUrl", longUrl);
//        map.put("shortUrl", shortUrl);
//        map.put("ttl", ttl);
//        try {
//            return namedParameterJdbcTemplate.update(sql, map);
//        } catch (DataAccessException e) {
//            return null;
//        }
//    }

    @Override
    public Integer saveUrlInfo(String longUrl,int ttl){
        String sql = "INSERT INTO info (long_url,ttl) VALUES (:longUrl, :ttl)";
        Map<String, Object> map = new HashMap<>();
        map.put("longUrl", longUrl);
        map.put("ttl", ttl);
        // 使用 KeyHolder 來捕獲自增 ID
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder, new String[]{"id"});
            return keyHolder.getKey().intValue();
            // 執行插入操作並獲取自增 ID
        } catch (DataIntegrityViolationException | NullPointerException e) {
            return null; // 插入失敗時返回 null
        }
    }

    @Override
    public String findLongUrl(String longUrl) {
        String sql = "SELECT short_url FROM info WHERE long_url=:longUrl";
        Map<String, Object> map = new HashMap<>();
        map.put("longUrl", longUrl);
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, map, String.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String findLongUrlById(String id) {
        String sql = "SELECT long_url FROM info WHERE id=:id";
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, map, String.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


}
