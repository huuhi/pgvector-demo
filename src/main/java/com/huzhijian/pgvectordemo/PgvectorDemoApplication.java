package com.huzhijian.pgvectordemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.huzhijian.pgvectordemo.mapper")
public class PgvectorDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PgvectorDemoApplication.class, args);
    }

}
