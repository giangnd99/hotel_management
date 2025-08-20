package com.poly.customercontainer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.poly")
@EnableFeignClients(basePackages = "com.poly.customerdataaccess.feign")
@EnableDiscoveryClient
@EnableJpaRepositories(basePackages = "com.poly.customerdataaccess.repository")
@EntityScan(basePackages = "com.poly.customerdataaccess.entity")
@ComponentScan(basePackages = "com.poly")
public class CustomerContainerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerContainerApplication.class, args);
    }

}
