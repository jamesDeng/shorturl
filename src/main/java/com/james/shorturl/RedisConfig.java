package com.james.shorturl;

import java.io.IOException;

import com.james.shorturl.utils.FastJson2JsonRedisSerializer;
import com.james.shorturl.utils.LongRedisSerializer;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * @author hm
 * @date 2019年2月26日
 * @Describe
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory,
        RedisSerializer fastJson2JsonRedisSerializer) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(factory);

        //key采用String序列化方式
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);

        //value采用fast-json序列化方式。
        template.setValueSerializer(fastJson2JsonRedisSerializer);
        template.setHashValueSerializer(fastJson2JsonRedisSerializer);

        template.afterPropertiesSet();
        return template;
    }

    @Bean(name = "longRedisTemplate")
    public RedisTemplate<String, Long> longRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Long> template = new RedisTemplate();
        template.setConnectionFactory(factory);

        //key采用String序列化方式
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);

        //value采用Long
        LongRedisSerializer longRedisSerializer = new LongRedisSerializer();
        template.setValueSerializer(longRedisSerializer);
        template.setHashValueSerializer(longRedisSerializer);

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisSerializer fastJson2JsonRedisSerializer() {
        return new FastJson2JsonRedisSerializer<>(Object.class);
    }

    /**
     * two set script
     *
     * @return
     */
    @Bean
    public RedisScript<Integer> twoSet() throws IOException {

        ScriptSource scriptSource = new ResourceScriptSource(new ClassPathResource("scripts/twoSet.lua"));

        return RedisScript.of(scriptSource.getScriptAsString(), Integer.class);
    }

}

