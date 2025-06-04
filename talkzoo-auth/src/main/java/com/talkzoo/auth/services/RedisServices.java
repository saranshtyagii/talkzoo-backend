package com.talkzoo.auth.services;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisServices {

    private final RedisTemplate<String, Object> redisTemplate;


    public RedisServices(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, Object value, Long expireTimeInSeconds) {
        try {
            redisTemplate.opsForValue().set(key, value, expireTimeInSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            // ignore
        }
    }

    public String get(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        return value != null ? value.toString() : null;
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

}
