package com.poly.room.management.domain.config;

import com.poly.room.management.domain.service.RoomDomainService;
import com.poly.room.management.domain.service.impl.RoomDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainBeanConfiguration {

    @Bean
    public RoomDomainService getDomainService() {
        return new RoomDomainServiceImpl();
    }
}
