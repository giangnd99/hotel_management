package com.poly.authentication.service.domain;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@EntityScan(basePackages = {"com.poly"})
@EnableJpaRepositories(basePackages = {"com.poly.authentication.service.dao"})
@EnableRedisRepositories(basePackages = {"com.poly.authentication.service.dao.token.repository"})
@SpringBootApplication(scanBasePackages = "com.poly")
@ComponentScan(value = {"com.poly"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.poly.authentication.service.domain.port"})
public class AuthenticationApplication {
    public static void main(String[] args) {
        System.out.println("Welcome to Authentication Service " +
                "\n Your account is ready \n" +
                "\n username: admin@gmail.com \n" +
                "\n password: admin123\n this email is for test only");
        org.springframework.boot.SpringApplication.run(AuthenticationApplication.class, args);
    }
}
