package com.poly.ai.management.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {"com.poly.ai.management.dao"})
@EntityScan(basePackages = {"com.poly.ai.management.dao"})
@SpringBootApplication(scanBasePackages = "com.poly")
@EnableDiscoveryClient
public class AiManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiManagementApplication.class, args);
    }
}
