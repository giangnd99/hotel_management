package com.poly.paymentdataaccess.config;


import com.poly.paymentapplicationservice.port.input.ok2.CreateDepositPaymentLinkUsecase;
import com.poly.paymentapplicationservice.port.output.PaymentGateway;
import com.poly.paymentapplicationservice.service.ok2.CreateDepositPaymentLinkUsecaseImpl;
import com.poly.paymentdomain.output.InvoicePaymentRepository;
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
    public CreateDepositPaymentLinkUsecase createDepositPaymentLinkUsecase(
            PaymentRepository paymentRepository,
            InvoicePaymentRepository invoicePaymentRepository,
            PaymentGateway payOSClient
    ) {
        return  new CreateDepositPaymentLinkUsecaseImpl(paymentRepository,  invoicePaymentRepository, payOSClient);
    }
}
