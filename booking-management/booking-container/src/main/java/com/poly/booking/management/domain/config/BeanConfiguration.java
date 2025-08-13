package com.poly.booking.management.domain.config;

import com.poly.booking.management.domain.port.in.service.BookingManagementService;
import com.poly.booking.management.domain.service.BookingDomainService;
import com.poly.booking.management.domain.service.impl.BookingDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public BookingDomainService bookingDomainService() {
        return new BookingDomainServiceImpl();
    }
}
