package com.james.shorturl.utils;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.Assert;

import java.nio.charset.Charset;

/**
 * Long类型的RedisSerializer
 * @author RJH
 * @Date Created in 2018-06-01
 */
public class LongRedisSerializer implements RedisSerializer<Long> {
    /**
     * 字符编码
     */
    private final Charset charset;

    /**
     * 默认用UTF-8
     */
    public LongRedisSerializer() {
        this(Charset.forName("UTF8"));
    }

    public LongRedisSerializer(Charset charset) {
        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
    }

    @Override
    public byte[] serialize(Long aLong) throws SerializationException {
        return (aLong == null ? null : String.valueOf(aLong).getBytes(charset));
    }

    @Override
    public Long deserialize(byte[] bytes) throws SerializationException {
        Long result=null;
        if(bytes!=null){
            String str=new String(bytes,charset);
            result=Long.valueOf(str);
        }
        return result;
    }
}