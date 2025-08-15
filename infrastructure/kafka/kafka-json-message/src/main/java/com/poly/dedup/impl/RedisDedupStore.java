package com.poly.dedup.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.dedup.DedupStore;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RedisDedupStore implements DedupStore {
    private final StringRedisTemplate redis;
    private final ObjectMapper mapper;

    public RedisDedupStore(StringRedisTemplate redis, ObjectMapper mapper) {
        this.redis = redis;
        this.mapper = mapper;
    }

    @Override
    public boolean markIfNotProcessed(String key, long ttlMillis) {
        ValueOperations<String, String> ops = redis.opsForValue();
        // setIfAbsent(key, "1", ttl, TimeUnit.MILLISECONDS) -> true if set, false if exists
        Boolean set = ops.setIfAbsent(key, "1", Duration.ofMillis(ttlMillis));
        return Boolean.TRUE.equals(set);
    }

    @Override
    public void putResponse(String correlationId, Object response, long ttlMillis) {
        try {
            String json = mapper.writeValueAsString(response);
            redis.opsForValue().set(responseKey(correlationId), json, Duration.ofMillis(ttlMillis));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T getResponse(String correlationId, Class<T> clazz) {
        String json = redis.opsForValue().get(responseKey(correlationId));
        if (json == null) return null;
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String responseKey(String correlationId) {
        return "RESP:" + correlationId;
    }
}
