package com.example.concurrentserver.service;

import com.example.concurrentserver.entity.Age;

import java.util.List;

public interface IAgeService {
    List<Age> getAge();
    int updateAge();
    int getMax();
    int getMaxLock();
    String testRedis();
    List<Age> redisCache();
    List<Age> redisCache2();
    int updateAgeRedis();
}
