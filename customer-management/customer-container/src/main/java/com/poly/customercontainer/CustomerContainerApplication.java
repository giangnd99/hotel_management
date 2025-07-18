package com.poly.customercontainer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.poly")
@EnableFeignClients(basePackages = "com.poly.customerdataaccess.feign")
public class CustomerContainerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerContainerApplication.class, args);
    }

}
