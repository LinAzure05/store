package com.crafts.platform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.crafts.platform.mapper")
@SpringBootApplication
public class CraftsPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(CraftsPlatformApplication.class, args);
    }
}
