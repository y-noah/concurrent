package com.example.concurrentserver.controller;

import com.example.concurrentserver.entity.Test;
import com.example.concurrentserver.entity.TestJoinAge;
import com.example.concurrentserver.service.ITestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Controller
@RestController
@RequestMapping("/select")
public class SelectController {
    @Autowired
    private ITestService iTestService;

    private static final Logger logger = LoggerFactory.getLogger(SelectController.class);

    @PostMapping("/getByAge")
    public List<Test> getByAge(Integer age) {
        List<Test> byAge = iTestService.getByAge(age);

        return byAge;
    }

    @PostMapping("/getByPage")
    public List<Test> getByPage(Integer size, Integer offset) {
        long l1 = System.currentTimeMillis();
        List<Test> byPage = iTestService.getByPage(size, offset);
        long l2 = System.currentTimeMillis();
        logger.info("耗时：" + (l2 - l1) + "ms");

        // 用分页插件需要添加拦截器
        // IPage<Test> byPage = iTestService.getByPage(size, offset);

        return byPage;
    }

    @PostMapping("/getTestJoinAge")
    public List<TestJoinAge> getTestJoinAge() {
        long l1 = System.currentTimeMillis();
        List<TestJoinAge> joinAge = iTestService.getTestJoinAge();
        long l2 = System.currentTimeMillis();
        logger.info("耗时：" + (l2 - l1) + "ms");

        return joinAge;
    }

    @PostMapping("/testExecutor")
    public void testExecutor() {
        // 定长线程池
        ExecutorService fixedPool = Executors.newFixedThreadPool(3);
        fixedPool.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("fixedPool submit Thread: " + Thread.currentThread().getName() + System.currentTimeMillis());
            }
        });
        fixedPool.shutdown();


        // 单个线程池
        ExecutorService singlePool = Executors.newSingleThreadExecutor();
        singlePool.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("singlePool submit Thread: " + Thread.currentThread().getName() + System.currentTimeMillis());
            }
        });
        singlePool.shutdown();



        // 可缓存线程池
        ExecutorService cachePool = Executors.newCachedThreadPool();
        cachePool.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("cachePool submit Thread: " + Thread.currentThread().getName() + System.currentTimeMillis());
            }
        });
        cachePool.shutdown();



        // 周期任务的线程池
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("scheduleAtFixedRate Thread: " + Thread.currentThread().getName() + System.currentTimeMillis());
            }
        }, 1,5, TimeUnit.SECONDS);
        scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("schedule Thread: " + Thread.currentThread().getName() + System.currentTimeMillis());
            }
        }, 3, TimeUnit.SECONDS);
    }
}
