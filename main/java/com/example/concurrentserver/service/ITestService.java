package com.example.concurrentserver.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.concurrentserver.entity.Test;
import com.example.concurrentserver.entity.TestJoinAge;

import java.util.List;

public interface ITestService {

    List<Test> getByAge(Integer age);
    List<Test> getByPage(Integer size, Integer offset);
    List<TestJoinAge> getTestJoinAge();
}
