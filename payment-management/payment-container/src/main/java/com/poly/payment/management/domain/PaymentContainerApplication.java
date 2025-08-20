package com.poly.payment.management.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaRepositories(basePackages = "com.poly")
@EntityScan(basePackages = "com.poly")
@EnableScheduling
@ComponentScan(basePackages = {"com.poly"})
@SpringBootApplication(scanBasePackages = "com.poly")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.poly")
public class PaymentContainerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentContainerApplication.class, args);
    }

}
