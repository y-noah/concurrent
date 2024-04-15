package com.example.concurrentserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.concurrentserver.entity.Age;
import com.example.concurrentserver.entity.Test;
import com.example.concurrentserver.entity.TestJoinAge;
import com.example.concurrentserver.mapper.AgeMapper;
import com.example.concurrentserver.mapper.TestMapper;
import com.example.concurrentserver.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestServiceImpl implements ITestService {
    @Autowired
    private TestMapper testMapper;
    private AgeMapper ageMapper;

    @Override
    public List<Test> getByAge(Integer age) {
        return testMapper.getByAge(age);
    }

    @Override
    public List<Test> getByPage(Integer size, Integer offset) {
        return testMapper.getByPage(size, offset);
    }

    @Override
    public List<TestJoinAge> getTestJoinAge() {
        QueryWrapper<Test> testQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Age> ageQueryWrapper = new QueryWrapper<>();

        List<Test> tests = testMapper.selectList(testQueryWrapper);
        List<Age> ages = ageMapper.selectList(ageQueryWrapper);

        return tests.stream()
                .flatMap(test -> ages.stream()
                        .filter(age -> age.getMin() <= test.getAge() && test.getAge() <= age.getMax())
                        .map(age -> {
                            TestJoinAge testJoinAge = new TestJoinAge();
                            testJoinAge.setId(test.getId());
                            testJoinAge.setName(test.getName());
                            testJoinAge.setAge(test.getAge());
                            testJoinAge.setText(test.getText());
                            testJoinAge.setType(age.getType());
                            return testJoinAge;
                        }))
                .collect(Collectors.toList());
    }

//    @Override
//    public IPage<Test> getByPage(Integer size, Integer offset) {
//        Page<Test> page = new Page<>(offset, size);
//        return testMapper.selectPage(page, null);
//    }
}
