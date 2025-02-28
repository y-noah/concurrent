package com.example.concurrentserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
public class AgeRedisMessagePublisher {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private ChannelTopic topic = new ChannelTopic("Cache1");

    public void publish(String message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}