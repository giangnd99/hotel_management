package com.poly.paymentcontainer.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotentConfig {

    @Bean
    public Dotenv dotenv() {
        return Dotenv.configure()
                .filename("key.env")
                .directory("./")
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();
    }
}
