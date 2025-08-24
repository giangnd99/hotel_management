package com.poly.ai.management.domain.config;

import com.poly.ai.management.domain.AiDomainService;
import com.poly.ai.management.domain.AiDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfiguration {

    @Bean
    public AiDomainService getAiDomainService() {
        return new AiDomainServiceImpl();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

