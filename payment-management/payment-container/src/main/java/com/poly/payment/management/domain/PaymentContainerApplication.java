package com.poly.payment.management.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaRepositories(basePackages = "com.poly.payment.management.data.access")
@EntityScan(basePackages = "com.poly.payment.management.data.access")
@EnableScheduling
@ComponentScan(basePackages = {"com.poly"})
@SpringBootApplication(scanBasePackages = "com.poly")
@EnableDiscoveryClient
public class PaymentContainerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentContainerApplication.class, args);
    }

}
