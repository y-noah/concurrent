package com.example.concurrentserver.config;


import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//拦截器
@Configuration  //第一步：配置类  交给Spring管理  确保在启动类的包或子包下，才能被扫描到
public class Interceptor {
    // 第二步：做对应Bean
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        // 第三步：创建拦截器（这只是一个壳子）
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 第四步：添加内部拦截器  (分页的）
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        //  可以添加多个内部拦截器
        return interceptor;
    }
}
