package com.poly.notification.management.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = "com.poly.notification.management.entity")
@EnableJpaRepositories(basePackages = "com.poly.notification.management.repository")
@ComponentScan(basePackages = {"com.poly"})
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.poly")
public class NotificationManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationManagementApplication.class, args);
    }
}
