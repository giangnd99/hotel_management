package com.poly.paymentcontainer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.poly")
public class PaymentContainerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentContainerApplication.class, args);
    }

}
