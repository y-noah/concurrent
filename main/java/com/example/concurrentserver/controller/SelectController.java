package com.example.concurrentserver.controller;

import com.example.concurrentserver.entity.Test;
import com.example.concurrentserver.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RestController
@RequestMapping("/select")
public class SelectController {
    @Autowired
    private ITestService iTestService;

    @PostMapping("/getByAge")
    public List<Test> getByAge(Integer age) {
        List<Test> byAge = iTestService.getByAge(age);

        return byAge;
    }

}
