package com.poly.restaurant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.poly.restaurant.dataaccess")
@EntityScan(basePackages = "com.poly.restaurant.dataaccess")
@SpringBootApplication(scanBasePackages = "com.poly")
@EnableFeignClients(basePackages = "com.poly.restaurant.application.port")
public class RestaurantApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestaurantApplication.class, args);
    }
}
