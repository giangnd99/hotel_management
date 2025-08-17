package com.poly.customerdataaccess.config;

import com.poly.customerapplicationservice.port.input.CustomerUsecase;
import com.poly.customerapplicationservice.port.input.LoyaltyUsecase;
import com.poly.customerapplicationservice.port.output.PromotionServicePort;
import com.poly.customerapplicationservice.service.CustomerApplicationService;
import com.poly.customerapplicationservice.service.LoyaltyApplicationService;
import com.poly.customerdomain.output.CustomerRepository;
import com.poly.customerdomain.output.LoyaltyPointRepository;
import com.poly.customerdomain.output.LoyaltyTransactionRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
public class UsecaseBeanConfig {

    @Bean
    public CustomerUsecase customerUsecase(CustomerRepository customerRepo,
                                           LoyaltyPointRepository loyaltyRepo) {
        return new CustomerApplicationService(customerRepo, loyaltyRepo);
    }

    @Bean
    public LoyaltyUsecase loyaltyUsecase(CustomerRepository customerRepo,
                                         LoyaltyPointRepository loyaltyPointRepository,
                                         LoyaltyTransactionRepository loyaltyTransactionRepository, PromotionServicePort promotionServicePort) {
        return new LoyaltyApplicationService(customerRepo, loyaltyPointRepository, loyaltyTransactionRepository, promotionServicePort);
    }


}
