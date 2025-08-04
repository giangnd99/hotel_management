package com.poly.swaggerhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SwaggerHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwaggerHubApplication.class, args);
    }
} 