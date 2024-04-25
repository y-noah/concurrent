package com.example.concurrentserver.config;

import com.example.concurrentserver.entity.Age;
import com.example.concurrentserver.mapper.AgeMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AgeRedisMessageListener implements MessageListener {

    @Autowired
    private AgeMapper ageMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 从消息中获取操作类型
        String operation = new String(message.getBody());

        // 根据操作类型更新Redis缓存
        if (operation.equals("INSERT") || operation.equals("UPDATE") || operation.equals("DELETE")) {
            List<Age> ages = ageMapper.selectList(null);
            // 获取 Redis 操作对象
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

            // 将查询到的 Age 对象列表转换为 JSON 字符串
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String json = objectMapper.writeValueAsString(ages);

                // 将 JSON 字符串存储到 Redis 中
                valueOperations.set("Cache", json);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }
}
