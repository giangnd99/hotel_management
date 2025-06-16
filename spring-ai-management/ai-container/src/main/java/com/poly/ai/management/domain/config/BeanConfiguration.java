package com.poly.ai.management.domain.config;

import com.poly.ai.management.domain.AiDomainService;
import com.poly.ai.management.domain.AiDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public AiDomainService getAiDomainService() {
        return new AiDomainServiceImpl();
    }
}
