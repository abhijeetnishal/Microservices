package com.microservices.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import io.sentry.Sentry;
import io.sentry.SentryLevel;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private final Logger logger = Logger.getLogger(RedisService.class.getName());

    public void setKey(String key, String value, long expirationTimeInSeconds) {
        try {
            stringRedisTemplate.opsForValue().set(key, value, expirationTimeInSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            Sentry.captureMessage("Error occurred while setting Redis key: " + e.getMessage(), SentryLevel.FATAL);
            logger.severe("Error occurred while setting Redis key: " + e.getMessage());
        }
    }

    public String getKey(String key) {
        try {
            return stringRedisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            Sentry.captureMessage("Error occurred while getting Redis key: " + e.getMessage(), SentryLevel.FATAL);
            logger.severe("Error occurred while getting Redis key: " + e.getMessage());
            return null;
        }
    }

    public void deleteKey(String key) {
        try {
            stringRedisTemplate.delete(key);
        } catch (Exception e) {
            Sentry.captureMessage("Error occurred while deleting Redis key: " + e.getMessage(), SentryLevel.FATAL);
            logger.severe("Error occurred while deleting Redis key: " + e.getMessage());
        }
    }
}
