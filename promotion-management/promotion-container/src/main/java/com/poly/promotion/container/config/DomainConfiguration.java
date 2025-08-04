package com.poly.promotion.container.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfiguration {
    @Bean
    public PromotionApi promotionApi(){
        return new PromotionApiImpl();
    }
}
