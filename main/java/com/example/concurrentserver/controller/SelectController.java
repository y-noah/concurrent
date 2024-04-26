package com.example.concurrentserver.controller;

import com.example.concurrentserver.entity.Age;
import com.example.concurrentserver.entity.Test;
import com.example.concurrentserver.entity.TestJoinAge;
import com.example.concurrentserver.service.IAgeService;
import com.example.concurrentserver.service.ITestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.*;

@Controller
@RestController
@RequestMapping("/select")
public class SelectController {
    @Autowired
    private ITestService iTestService;
    @Autowired
    private IAgeService iAgeService;

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

    @PostMapping("testRoutine")
    public void testRoutine() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                List<Age> age = iAgeService.getAge();
                for (Age a: age) {
                    System.out.println(a.toString());
                }
            }
        }, 0, 2, TimeUnit.SECONDS);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                int max = iAgeService.updateAge();
                System.out.println("max" + max);
            }
        }, 0, 2, TimeUnit.SECONDS);
    }

    @PostMapping("testConcurrent")
    public void testConcurrent() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

        Runnable task = () -> {
             int max = iAgeService.getMax();
             System.out.println("max:" + max);
            if (max <= 0) {
                scheduledExecutorService.shutdown();
            }
        };
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(task, 0, 2, TimeUnit.SECONDS);
    }

    @PostMapping("testConcurrentLock")
    public void testConcurrentLock() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

        Runnable task = () -> {
            int max = iAgeService.getMaxLock();
            System.out.println("max:" + max);
            if (max <= 0) {
                scheduledExecutorService.shutdown();
            }
        };
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(task, 0, 2, TimeUnit.SECONDS);
    }

    @PostMapping("testRedis")
    public void testRedis() {
        System.out.println("text:" + iAgeService.testRedis());
    }

    @PostMapping("redisCache")
    public void redisCache() {
        System.out.println("text:" + iAgeService.redisCache());
    }

    @PostMapping("redisCache2")
    public void redisCache2() {
        System.out.println("text:" + iAgeService.redisCache2());
    }

    @PostMapping("redisUpdate")
    public void redisUpdate() {
        int i = iAgeService.updateAgeRedis();
        System.out.println("RedisI:" + i);
    }
}
