package com.poly.customercontainer.config;

import com.poly.customerapplicationservice.port.input.CustomerUsecase;
import com.poly.customerapplicationservice.port.input.LoyaltyUsecase;
import com.poly.customerapplicationservice.service.CustomerApplicationService;
import com.poly.customerapplicationservice.service.LoyaltyApplicationSerivce;
import com.poly.customerdomain.output.CustomerRepository;
import com.poly.customerdomain.output.LoyaltyRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.poly.customerdataaccess.jpa")
@EntityScan(basePackages = "com.poly.customerdataaccess.entity")
@ComponentScan(basePackages = {
        "com.poly.customerdataaccess"})
public class BeanConfig {

    @Bean
    public CustomerUsecase customerUsecase(CustomerRepository customerRepo,
                                           LoyaltyRepository loyaltyRepo) {
        return new CustomerApplicationService(customerRepo, loyaltyRepo);
    }

    @Bean
    public LoyaltyUsecase loyaltyUsecase(CustomerRepository customerRepo,
                                         LoyaltyRepository loyaltyRepo) {
        return new LoyaltyApplicationSerivce(loyaltyRepo, customerRepo);
    }

}
