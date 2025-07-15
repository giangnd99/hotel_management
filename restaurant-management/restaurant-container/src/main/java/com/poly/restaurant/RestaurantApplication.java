package com.poly.restaurant;

import com.poly.restaurant.application.annotation.EnableDomainHandlers;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDomainHandlers("com.poly.restaurant.application.handler.impl")
public class RestaurantApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestaurantApplication.class, args);
    }
}
