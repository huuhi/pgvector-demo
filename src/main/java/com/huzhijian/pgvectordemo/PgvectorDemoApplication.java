package com.huzhijian.pgvectordemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@MapperScan("com.huzhijian.pgvectordemo.mapper")
@EnableAsync
public class PgvectorDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PgvectorDemoApplication.class, args);
    }

}
