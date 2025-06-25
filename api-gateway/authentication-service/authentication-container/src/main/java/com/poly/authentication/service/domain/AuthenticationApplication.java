package com.poly.authentication.service.domain;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@EntityScan(basePackages = {"com.poly.authentication.service.dao"})
@EnableJpaRepositories(basePackages = {"com.poly.authentication.service.dao"})
@EnableRedisRepositories(basePackages = {"com.poly.authentication.service.dao.token.repository"})
@SpringBootApplication(scanBasePackages = "com.poly")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.poly.authentication.service.domain.port"})
public class AuthenticationApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(AuthenticationApplication.class, args);
    }
}
