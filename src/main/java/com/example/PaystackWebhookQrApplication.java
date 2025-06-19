package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PaystackWebhookQrApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaystackWebhookQrApplication.class, args);
    }
} 