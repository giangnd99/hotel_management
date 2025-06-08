package com.poly.booking.management.domain;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.poly"})
public class BookingManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookingManagementApplication.class, args);
    }
}
