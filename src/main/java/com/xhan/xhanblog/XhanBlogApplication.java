package com.xhan.xhanblog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.xhan.xhanblog.repository")
public class XhanBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(XhanBlogApplication.class, args);
    }

}
