package org.example.base62.service;

import io.seruco.encoding.base62.Base62;
import org.example.base62.dao.UrlDao;
import org.example.base62.dto.shortenRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UrlServiceImpl implements UrlService {
    private static final String BASE_URL = "http://localhost:8080/";
    private static final String BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private static final Logger log = LoggerFactory.getLogger(UrlServiceImpl.class);

    @Autowired
    UrlDao urlDao;

    //長網址轉短網址主要邏輯
    @Override
    public String shortenUrl(shortenRequest shortenRequest){
        //String shortUrl = BASE_URL+ shortKey;

        Integer id = urlDao.saveUrlInfo(shortenRequest.getLongUrl(),shortenRequest.getTtl());

        if(id == null){
            return null;
        }

        String formattedId = String.format("%05d", id);
        log.info("Generated shortened url id: " + formattedId);
        Base62 base62 = Base62.createInstance();
        byte[] encoded = base62.encode(formattedId.getBytes());
        log.info(new String(encoded));
        return BASE_URL+new String(encoded);

    }

    @Override
    public String getLongUrl(String shortKey) {
        // 兩種結果 1.longUrl(成功取得 url) 2.null(沒有該url)
        Base62 base62 = Base62.createInstance();
        byte[] decoded = base62.decode(shortKey.getBytes());
        String id = new String(decoded).replaceFirst("^0+(?!$)", "");
        return urlDao.findLongUrlById(id);
    }



}