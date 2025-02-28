package com.example.concurrentserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.concurrentserver.entity.Test;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TestMapper extends BaseMapper<Test> {
    List<Test> getByAge(@Param("age") Integer age);
    List<Test> getByPage(@Param("size") Integer size, @Param("offset") Integer offset);
    List<Test> getTestJoinAge();
}
