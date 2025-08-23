package com.poly.room.management.domain.config;

import com.poly.room.management.domain.service.RoomDomainService;
import com.poly.room.management.domain.service.RoomEventService;
import com.poly.room.management.domain.service.impl.RoomCommandServiceImpl;
import com.poly.room.management.domain.service.impl.RoomDomainServiceImpl;
import com.poly.room.management.domain.service.impl.RoomEventServiceImpl;
import com.poly.room.management.domain.service.impl.RoomQueryServiceImpl;
import com.poly.room.management.domain.service.sub.RoomCommandService;
import com.poly.room.management.domain.service.sub.RoomQueryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainBeanConfiguration {

    @Bean
    public RoomDomainService getDomainService() {
        return new RoomDomainServiceImpl();
    }

    @Bean
    public RoomCommandService getCommandService() {
        return new RoomCommandServiceImpl();
    }

    @Bean
    public RoomQueryService getQueryService() {
        return new RoomQueryServiceImpl();
    }

    @Bean
    public RoomEventService getEventService() {
        return new RoomEventServiceImpl();
    }
}
