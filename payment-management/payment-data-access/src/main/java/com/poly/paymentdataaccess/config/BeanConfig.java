package com.poly.paymentdataaccess.config;

import com.poly.paymentapplicationservice.port.input.InvoiceUsecase;
import com.poly.paymentapplicationservice.port.input.PaymentUsecase;
import com.poly.paymentapplicationservice.port.output.PaymentGateway;
import com.poly.paymentapplicationservice.service.InvoiceApplicationService;
import com.poly.paymentapplicationservice.service.PaymentApplicationService;
import com.poly.paymentdomain.output.InvoiceItemRepository;
import com.poly.paymentdomain.output.InvoiceRepository;
import com.poly.paymentdomain.output.PaymentRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.poly.paymentdataaccess.repository")
@EntityScan(basePackages = "com.poly.paymentdataaccess.entity")
@ComponentScan(basePackages = {"com.poly.paymentdataaccess"})
public class BeanConfig {

    @Bean
    public PaymentUsecase paymentUsecase(PaymentRepository paymentRepository, InvoiceRepository invoiceRepository, PaymentGateway payOS) {
        return new PaymentApplicationService(paymentRepository, invoiceRepository, payOS);
    }

    @Bean
    public InvoiceUsecase invoiceUsecase(InvoiceRepository invoiceRepository, PaymentRepository paymentRepository) {
        return new InvoiceApplicationService(invoiceRepository, paymentRepository);
    }
}
