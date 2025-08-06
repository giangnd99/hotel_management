package com.poly.promotion.container.config;

import com.poly.promotion.domain.application.api.PromotionApi;
import com.poly.promotion.domain.application.api.impl.PromotionApiImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfiguration {
    @Bean
    public PromotionApi promotionApi(){
        return new PromotionApiImpl();
    }
}
