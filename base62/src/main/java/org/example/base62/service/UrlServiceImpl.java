package org.example.base62.service;

import org.example.base62.dao.UrlDao;
import org.example.base62.dto.shortenRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UrlServiceImpl implements UrlService {
    private static final String BASE_URL = "http://localhost:8080/";
    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int KEY_LENGTH = 7;

    private static final Logger log = LoggerFactory.getLogger(UrlServiceImpl.class);

    @Autowired
    UrlDao urlDao;

    //長網址轉短網址主要邏輯
    @Override
    public String shortenUrl(shortenRequest shortenRequest) {
        String shortKey = generateShortUrl(shortenRequest.getLongUrl());
        String shortUrl = BASE_URL + shortKey;
        boolean state = true;

        try {
            //確認資料庫是否已有該長網址轉換之結果
            String findLongUrlResult = urlDao.findLongUrl(shortenRequest.getLongUrl());
            if (findLongUrlResult != null) {
                return findLongUrlResult;
            }
            //確保插入的數據不會重複
            while (state) {
                if(urlDao.saveUrlInfo(shortenRequest.getLongUrl(), shortUrl, shortenRequest.getTtl()) == 1){
                    state = false;
                }
            }
            return shortUrl;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }

    }

    @Override
    public String getLongUrl(String shortKey) {
        // 兩種結果 - 1.longUrl(成功取得 url) 2.null(沒有該url)
        return urlDao.findLongUrlByShortUrl(BASE_URL + shortKey);
    }

    private String generateShortUrl(String longUrl) {
        long asciiValue = getAsciiValue(longUrl);
        String base62Encoded = encodeBase62(asciiValue);
        return getRandomSubstring(base62Encoded);
    }

    private long getAsciiValue(String input) {
        byte[] bytes = input.getBytes();
        long asciiValue = 0;
        for (byte b : bytes) {
            asciiValue = (asciiValue * 256) + (b & 0xFF);
        }
        return asciiValue;
    }

    private String encodeBase62(long value) {
        StringBuilder sb = new StringBuilder();
        while (value > 0) {
            sb.append(BASE62.charAt((int) (value % 62)));
            value /= 62;
        }
        return sb.reverse().toString();
    }

    private String getRandomSubstring(String input) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < KEY_LENGTH; i++) {
            sb.append(input.charAt(random.nextInt(input.length())));
        }
        return sb.toString();
    }
}