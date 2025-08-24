package com.poly.authentication.service.domain.config;

import com.poly.authentication.service.domain.service.AuthDomainService;
import com.poly.authentication.service.domain.service.impl.AuthDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public AuthDomainService domainService() {
        return new AuthDomainServiceImpl();
    }
}
