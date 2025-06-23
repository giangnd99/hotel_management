package com.poly.authentication.service.domain;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {"com.poly.authentication.service.dao"})
@EnableJpaRepositories(basePackages = {"com.poly.authentication.service.dao"})
@SpringBootApplication(scanBasePackages = "com.poly")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.poly.authentication.service.domain.port"})
public class AuthenticationApplication {
}
