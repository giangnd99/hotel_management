package com.poly.booking.management.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {"com.poly"})
@EnableJpaRepositories(basePackages = {"com.poly"})
@SpringBootApplication(scanBasePackages = "com.poly")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.poly.booking.management.domain.port"})
public class BookingManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookingManagementApplication.class, args);
    }
}
