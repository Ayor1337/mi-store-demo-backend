package com.example.util;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class FlowUtils {

    @Resource(name = "stringRedisTemplate")
    StringRedisTemplate redisTemplate;

    public boolean limitOnceCheck(String key, int blockTime) {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            return false;
        } else {
            redisTemplate.opsForValue().set(key, "", blockTime, TimeUnit.SECONDS);
            return true;
        }
    }
}
