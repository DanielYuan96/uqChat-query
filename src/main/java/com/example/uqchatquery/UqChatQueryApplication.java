package com.example.uqchatquery;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.example.uqchatquery.dao.mapper")
public class UqChatQueryApplication {

    public static void main(String[] args) {
        SpringApplication.run(UqChatQueryApplication.class, args);
    }

}
