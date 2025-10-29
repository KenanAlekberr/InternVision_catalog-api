package com.example.catalogapi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static lombok.AccessLevel.PRIVATE;

@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public final class CacheUtilWithRedisson {
    RedissonClient redissonClient;
    ObjectMapper mapper;

    @SneakyThrows
    public <T> void set(String key, T data, int timeToLive, TimeUnit timeUnit) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        String serialized = mapper.writeValueAsString(data);
        bucket.set(serialized, java.time.Duration.ofMillis(timeUnit.toMillis(timeToLive)));
    }

    @SneakyThrows
    public <T> T get(String key, Class<T> clazz) {
        RBucket<Object> bucket = redissonClient.getBucket(key);

        if (!bucket.isExists())
            return null;

        return mapper.readValue((String) bucket.get(), clazz);
    }

    public void delete(String key) {
        RBucket<Object> bucket = redissonClient.getBucket(key);

        if (bucket.isExists())
            bucket.delete();
    }

    public void deleteByPattern(String pattern) {
        redissonClient.getKeys().deleteByPattern(pattern);
    }
}