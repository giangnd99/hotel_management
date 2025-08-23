package com.poly.promotion.container.config;

import com.poly.promotion.domain.application.api.external.VoucherExternalApi;
import com.poly.promotion.domain.application.api.external.VoucherPackExternalApi;
import com.poly.promotion.domain.application.api.external.impl.VoucherExternalApiImpl;
import com.poly.promotion.domain.application.api.external.impl.VoucherPackExternalApiImpl;
import com.poly.promotion.domain.application.api.internal.VoucherPackInternalApi;
import com.poly.promotion.domain.application.api.internal.impl.VoucherPackInternalApiImpl;
import com.poly.promotion.domain.application.service.ExpirationManagementService;
import com.poly.promotion.domain.application.service.VoucherPackService;
import com.poly.promotion.domain.application.service.VoucherService;
import com.poly.promotion.domain.application.service.impl.ExpirationManagementServiceImpl;
import com.poly.promotion.domain.application.service.impl.VoucherPackServiceImpl;
import com.poly.promotion.domain.application.service.impl.VoucherServiceImpl;
import com.poly.promotion.domain.application.spi.repository.VoucherPackRepository;
import com.poly.promotion.domain.application.spi.repository.VoucherRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfiguration {

    @Bean
    public VoucherExternalApi voucherExternalApi(VoucherService voucherService){
        return new VoucherExternalApiImpl(voucherService);
    }

    @Bean
    public VoucherPackExternalApi voucherPackExternalApi(VoucherPackService voucherPackService){
        return new VoucherPackExternalApiImpl(voucherPackService);
    }

    @Bean
    public VoucherPackInternalApi voucherPackInternalApi(VoucherPackService voucherPackService){
        return new VoucherPackInternalApiImpl(voucherPackService);
    }
    
    @Bean
    public VoucherPackService voucherPackService(VoucherPackRepository voucherPackRepository) {
        return new VoucherPackServiceImpl(voucherPackRepository);
    }
    
    @Bean
    public VoucherService voucherService(VoucherPackService voucherPackService, VoucherRepository voucherRepository) {
        return new VoucherServiceImpl(voucherPackService, voucherRepository);
    }
    
    @Bean
    public ExpirationManagementService expirationManagementService(VoucherPackService voucherPackService, VoucherService voucherService) {
        return new ExpirationManagementServiceImpl(voucherPackService, voucherService);
    }
}
