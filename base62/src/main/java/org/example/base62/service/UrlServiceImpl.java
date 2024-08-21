package org.example.base62.service;

import io.seruco.encoding.base62.Base62;
import org.example.base62.dao.UrlDao;
import org.example.base62.dto.shortenRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UrlServiceImpl implements UrlService {
    private static final String BASE_URL = "http://localhost:8080/";
    private static final String BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int range = 15000;
    private static final Logger log = LoggerFactory.getLogger(UrlServiceImpl.class);

    @Autowired
    @Qualifier("namedParameterJdbcTemplate1")
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate1;

    @Autowired
    @Qualifier("namedParameterJdbcTemplate2")
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate2;

    @Autowired
    @Qualifier("namedParameterJdbcTemplate3")
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate3;

    @Autowired
    UrlDao urlDao;

    //長網址轉短網址主要邏輯
    @Override
    public String shortenUrl(shortenRequest shortenRequest){
        Random random = new Random();
        int randomResult = random.nextInt(2);
        NamedParameterJdbcTemplate shard =  getShardedTemplate(randomResult);
        Integer id = urlDao.saveUrlInfo(shard,shortenRequest.getLongUrl(),shortenRequest.getTtl());

        if(id == null){
            return null;
        }
        Base62 base62 = Base62.createInstance();
        switch (randomResult){
            case 0:
                String formattedId = String.format("%05d", id);
                log.info("Generated shortened url id: " + formattedId);
                byte[] encoded = base62.encode(formattedId.getBytes());
                return BASE_URL+new String(encoded);
            case 1:
                int shardId = id + range;
                log.info("Generated shortened url id: " + shardId);
                byte[] sharencoded = base62.encode(Integer.toString(shardId).getBytes());
                return BASE_URL+new String(sharencoded);
        }
        return null;
    }

    @Override
    public String getLongUrl(String shortKey) {
        // 兩種結果 1.longUrl(成功取得 url) 2.null(沒有該url)
        Base62 base62 = Base62.createInstance();
        byte[] decoded = base62.decode(shortKey.getBytes());
        String id = new String(decoded);

        if( Integer.parseInt(id) <= 15000 ){
            return urlDao.findLongUrlById(namedParameterJdbcTemplate1,id.replaceFirst("^0+(?!$)", ""));
        }
        return urlDao.findLongUrlById(namedParameterJdbcTemplate2,id);

    }

    private NamedParameterJdbcTemplate getShardedTemplate(int number) {
        switch (number) {
            case 0:
                return namedParameterJdbcTemplate1;
            case 1:
                return namedParameterJdbcTemplate2;
        }
        log.error("Wrong input!");
        return null;
    }



}