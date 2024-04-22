package com.example.concurrentserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.concurrentserver.entity.Age;
import com.example.concurrentserver.mapper.AgeMapper;
import com.example.concurrentserver.service.IAgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AgeServiceImpl implements IAgeService {
    @Autowired
    private AgeMapper ageMapper;

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
    public int getMax() {
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
    }
}
