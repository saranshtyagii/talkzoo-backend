package com.web.kafka.utils;

import com.web.kafka.utils.services.RedisServices;
import org.springframework.stereotype.Component;

@Component
public class RedisUtils {

    public static final long ONE_MINUTE = 60 * 1000;
    public static final long FIVE_MINUTES = 5 * ONE_MINUTE;
    public static final long FIFTEEN_MINUTES = 15 * ONE_MINUTE;
    public static final long TWENTY_MINUTES = 20 * ONE_MINUTE;
    public static final long THIRTY_MINUTES = 30 * ONE_MINUTE;
    public static final long ONE_HOUR = 60 * 60 * 1000;
    public static final long TWO_HOURS = 2 * ONE_HOUR;
    public static final long ONE_DAY = 24 * 60 * 60 * 1000;
    public static final long TWO_DAYS = 2 * ONE_DAY;
    public static final long THREE_DAYS = 3 * ONE_DAY;
    public static final long FOUR_DAYS = 3 * ONE_DAY;
    public static final long FIVE_DAYS = 3 * ONE_DAY;
    public static final long SIX_DAYS = 3 * ONE_DAY;
    public static final long ONE_WEEK = 7 * ONE_DAY;


    private final RedisServices redisServices;

    public RedisUtils(RedisServices redisServices) {
        this.redisServices = redisServices;
    }

    public void set(String key, Object value) {
        try {
            redisServices.set(key, value, TWO_HOURS);
        } catch (Exception e) {}
    }

    public void set(String key, Object value, Long expireTimeInSeconds) {
        try {
            redisServices.set(key, value, expireTimeInSeconds);
        } catch (Exception e) {}
    }

    public String get(String key) {
        try {
            return redisServices.get(key);
        } catch (Exception e) {}
        return null;
    }

}

