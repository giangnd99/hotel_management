package com.poly.room.management.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

//@EntityScan(basePackages = {"com.poly.ai.management.dao"})
@SpringBootApplication(scanBasePackages = "com.poly")
@EnableDiscoveryClient
public class RoomManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoomManagementApplication.class, args);
    }
}
