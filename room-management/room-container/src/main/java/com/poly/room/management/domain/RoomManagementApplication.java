package com.poly.room.management.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {"com.poly.room.management.dao"})
@EnableJpaRepositories(basePackages = {"com.poly"})
@SpringBootApplication(scanBasePackages = "com.poly")
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.poly"})
@EnableFeignClients(basePackages = {"com.poly.room.management.domain.port"})
public class RoomManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoomManagementApplication.class, args);
    }
}
