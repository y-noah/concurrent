package com.example.concurrentserver.service.impl;

import com.example.concurrentserver.entity.Test;
import com.example.concurrentserver.mapper.TestMapper;
import com.example.concurrentserver.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestServiceImpl implements ITestService {
    @Autowired
    private TestMapper testMapper;

    @Override
    public List<Test> getByAge(Integer age) {
        return testMapper.getByAge(age);
    }
}
