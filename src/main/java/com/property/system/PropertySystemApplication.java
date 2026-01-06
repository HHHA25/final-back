package com.property.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.property.system.mapper")
public class PropertySystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(PropertySystemApplication.class, args);
    }
}