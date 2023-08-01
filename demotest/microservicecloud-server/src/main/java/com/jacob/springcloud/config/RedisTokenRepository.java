package com.jacob.springcloud.config;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.jacob.springcloud.utils.JwtTokenUtil;

@Component
public class RedisTokenRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtTokenUtil jwtTokenUtil;

    public RedisTokenRepository(RedisTemplate<String, Object> redisTemplate, JwtTokenUtil jwtTokenUtil) {
        this.redisTemplate = redisTemplate;
        this.jwtTokenUtil = jwtTokenUtil;
    }
    /**
     * 前輟都加上user:
     * 
     */
    private String generateRedisKey(String subject) {
        return "user:" + subject;
    }


    /**
     * 從radis 檢查token
     * 
     */
    public boolean isTokenValid(String token) {
        String subject = jwtTokenUtil.getUsername(token);
        if (subject != null) {
            String redisKey = generateRedisKey(subject);
            String storedToken = (String) redisTemplate.opsForValue().get(redisKey);
            return storedToken != null && storedToken.equals(token);
        } else {
            return false;
        }
    }
    /**
     * 生成token
     * 
     */
    public void saveToken(String token) {
        String subject = jwtTokenUtil.getUsername(token);
        if (subject != null) {
            String redisKey = generateRedisKey(subject);
            long expiration = jwtTokenUtil.getExpirationTime(token) / 1000; // Convert expiration time to seconds
            redisTemplate.opsForValue().set(redisKey, token, expiration, TimeUnit.SECONDS);
        } else {
            throw new IllegalArgumentException("Invalid JWT token");
        }
    }
}
