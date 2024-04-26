package com.example.concurrentserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.concurrentserver.config.AgeRedisMessagePublisher;
import com.example.concurrentserver.entity.Age;
import com.example.concurrentserver.mapper.AgeMapper;
import com.example.concurrentserver.service.IAgeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AgeServiceImpl implements IAgeService {
    @Autowired
    private AgeMapper ageMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private AgeRedisMessagePublisher publisher;

    private final Object lock = new Object();

    /*
    * 1_1、Isolation.DEFAULT:为数据源的默认隔离级别
      1_2、isolation=Isolation.READ_UNCOMMITTED:未授权读取级别
      1_3、iIsolation.READ_COMMITTED:授权读取级别
      1_4、iIsolation.REPEATABLE_READ:可重复读取级别
      1_5、iIsolation.SERIALIZABLE:序列化级别
    */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<Age> getAge() {
        QueryWrapper<Age> ageQueryWrapper = new QueryWrapper<>();
        ageQueryWrapper.eq("id", 1);
        return ageMapper.selectList(ageQueryWrapper);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public int updateAge() {

        QueryWrapper<Age> ageQueryWrapper = new QueryWrapper<>();
        ageQueryWrapper.eq("id", 1);
        Age age = new Age();
        int max = (int) (Math.random()*100);
        age.setMax(max);
        ageMapper.update(age, ageQueryWrapper);
        return max;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public int getMax() {
        try {
            QueryWrapper<Age> ageQueryWrapper1 = new QueryWrapper<>();
            ageQueryWrapper1.eq("id", 1);
            List<Age> ages = ageMapper.selectList(ageQueryWrapper1);
            int m = ages.get(0).getMax();

            QueryWrapper<Age> ageQueryWrapper = new QueryWrapper<>();
            ageQueryWrapper.eq("id", 1);
            Age age = new Age();
            m = m - 1;
            age.setMax(m);
            ageMapper.update(age, ageQueryWrapper);

            return m;
        }catch (Exception e) {
            e.printStackTrace();
            return -9999;
        }
    }

    @Override
    public int getMaxLock() {
        synchronized (lock) {
            try {
                QueryWrapper<Age> ageQueryWrapper1 = new QueryWrapper<>();
                ageQueryWrapper1.eq("id", 1);
                List<Age> ages = ageMapper.selectList(ageQueryWrapper1);
                int m = ages.get(0).getMax();

                QueryWrapper<Age> ageQueryWrapper = new QueryWrapper<>();
                ageQueryWrapper.eq("id", 1);
                Age age = new Age();
                m = m - 1;
                age.setMax(m);
                ageMapper.update(age, ageQueryWrapper);

                return m;
            }catch (Exception e) {
                e.printStackTrace();
                return -9999;
            }
        }
    }

    @Override
    public String testRedis() {
        stringRedisTemplate.opsForValue().set("test", "9527");
        SetOperations<String, String> stringStringSetOperations = redisTemplate.opsForSet();
        stringStringSetOperations.add("set", "setTest");
        ListOperations<String, String> stringStringListOperations = redisTemplate.opsForList();
        stringStringListOperations.rightPush("list", "listTest");

        return stringRedisTemplate.opsForValue().get("test") + "," +
                stringStringSetOperations.members("set") + "," +
                stringStringListOperations.range("list",0,1);
    }

    @Override
    public List<Age> redisCache() {
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

        return ages;
    }

    @Override
    @Cacheable(value = "agesCache")
    public List<Age> redisCache2() {
        return ageMapper.selectList(null);
    }

    @Override
    public int updateAgeRedis() {
        QueryWrapper<Age> ageQueryWrapper = new QueryWrapper<>();
        ageQueryWrapper.eq("id", 1);
        Age age = new Age();
        int v = (int) (Math.random()*100);
        age.setMax(v);
        ageMapper.update(age, ageQueryWrapper);
        publisher.publish("UPDATE");

        return v;
    }
}
