/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.james.shorturl.biz;

import com.james.shorturl.utils.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 短链接服务
 *
 * 存储数据在 Redis, 2 key 存储一个短链接
 *
 * su:key:xddddddd=https://www.google.com
 * su:val:https://www.google.com=xddddddd
 *
 * @author yougao.dw
 * @date 2021/05/23
 */
@Service
public class ShorturlService {

    private static final Logger logger = LoggerFactory.getLogger(ShorturlService.class);

    /**
     *
     */
    private static final long SEQUENCE_START = 10000000000000L;

    private static final String SEQUENCE_KEY = "su:sequence";

    /**
     * f
     * key: original url
     * values: short url
     */
    private static final String SHORTURL_KEY = "su:key:";

    /**
     * key: short url
     * values: original url
     */
    private static final String SHORTURL_VALUE = "su:val:";

    @Value("${domain}")
    private String domain;

    @Autowired
    private RedisUtil redisUtil;

    public void initSequence() {
        Long result = redisUtil.getLong(SEQUENCE_KEY);
        if (result == null || result <= SEQUENCE_START) {
            logger.info("Init Sequence to {}", SEQUENCE_START);
            redisUtil.set(SEQUENCE_KEY, SEQUENCE_START);
        }
    }

    /**
     * Add short url
     *
     * @param originalUrl
     * @return ShortUrl
     */
    public String addShortUrl(String originalUrl) {

        String valKey = getShorturlValKey(originalUrl);
        String shortStr = redisUtil.getString(valKey);
        //存在直接返回
        if (shortStr != null) {
            return buildShortUrl(shortStr);
        }

        long sequenceVal = redisUtil.incr(SEQUENCE_KEY, 1L);

        shortStr = NumberUtils.encode10To62(sequenceVal);

        String keyKey = getShorturlKeyKey(shortStr);

        redisUtil.twoSet(valKey, shortStr, keyKey, originalUrl);

        return buildShortUrl(shortStr);
    }

    /**
     * get original Url
     *
     * @param shortStr
     * @return originalUrl
     */
    public String getOriginalUrl(String shortStr) {

        String keyKey = getShorturlKeyKey(shortStr);
        String originalUrl = redisUtil.getString(keyKey);
        return originalUrl;
    }

    private String getShorturlKeyKey(String shortStr) {
        StringBuilder sb = new StringBuilder(SHORTURL_KEY).append(shortStr);
        return sb.toString();
    }

    private String getShorturlValKey(String originalUrl) {
        StringBuilder sb = new StringBuilder(SHORTURL_VALUE).append(originalUrl);
        return sb.toString();
    }

    private String buildShortUrl(String shortStr) {
        StringBuilder sb = new StringBuilder(domain).append("/").append(shortStr);
        return sb.toString();
    }
}
