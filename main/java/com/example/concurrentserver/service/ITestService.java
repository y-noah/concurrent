package com.example.concurrentserver.service;

import com.example.concurrentserver.entity.Test;

import java.util.List;

public interface ITestService {

    List<Test> getByAge(Integer age);
}
