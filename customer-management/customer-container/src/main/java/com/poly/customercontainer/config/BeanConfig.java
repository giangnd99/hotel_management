package com.poly.customercontainer.config;

import com.poly.customerapplication.port.CustomerUsecase;
import com.poly.customerapplication.service.CustomerApplicationService;
import com.poly.customerdataaccess.port.CustomerRepositoryImpl;
import com.poly.customerdomain.output.CustomerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public CustomerRepository customerDomain() {
        return new CustomerRepositoryImpl();
    }

    @Bean
    public CustomerUsecase customerUsecase(CustomerRepository customerRepository) {
        return new CustomerApplicationService(customerRepository);
    }
}
