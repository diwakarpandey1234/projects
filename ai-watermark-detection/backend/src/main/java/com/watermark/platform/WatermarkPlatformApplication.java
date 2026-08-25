package com.watermark.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class WatermarkPlatformApplication {
    public static void main(String[] args) {
        SpringApplication.run(WatermarkPlatformApplication.class, args);
    }
}
