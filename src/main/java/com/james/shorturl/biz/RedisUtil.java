package com.james.shorturl.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redisTemplate封装
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisTemplate<String, Long> longRedisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    @Qualifier(value = "twoSet")
    private RedisScript<Integer> twoSet;

    /**
     * Two Set
     * 原子操作
     *
     * @param oneKye
     * @param oneVal
     * @param twoKey
     * @param twoVal
     * @return
     */
    public String twoSet(String oneKye, String oneVal, String twoKey, String twoVal) {
        List<String> keys = new ArrayList();
        keys.add(oneKye);
        keys.add(oneVal);
        keys.add(twoKey);
        keys.add(twoVal);

        Object result = stringRedisTemplate.execute(twoSet, keys);
        return result != null ? String.valueOf(result): null;
    }

    //============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * get long
     *
     * @param key 键
     * @return 值
     */
    public Long getLong(String key) {
        return longRedisTemplate.opsForValue().get(key);
    }

    /**
     * get long
     *
     * @param key 键
     * @return 值
     */
    public String getString(String key) {
        return stringRedisTemplate.boundValueOps(key).get();
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
        return true;
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {

        if (time > 0) {
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
        } else {
            set(key, value);
        }
        return true;
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }
}