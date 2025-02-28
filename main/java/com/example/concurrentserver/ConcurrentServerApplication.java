package com.example.concurrentserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.concurrentserver.mapper")
public class ConcurrentServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConcurrentServerApplication.class, args);
    }
}
