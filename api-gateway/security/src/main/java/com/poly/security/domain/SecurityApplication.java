package com.poly.security.domain;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;



@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.poly")
@EnableFeignClients
public class SecurityApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(SecurityApplication.class, args);
    }
}
