package com.poly.payment.management.domain.config;


import com.poly.payment.management.domain.port.input.service.*;
import com.poly.payment.management.domain.service.PaymentGateway;
import com.poly.payment.management.domain.port.output.repository.InvoicePaymentRepository;
import com.poly.payment.management.domain.port.output.repository.InvoiceRepository;
import com.poly.payment.management.domain.port.output.repository.PaymentRepository;
import com.poly.payment.management.domain.service.impl.*;
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
        return new CreateDepositPaymentLinkUsecaseImpl(paymentRepository, invoicePaymentRepository, payOSClient);
    }

    @Bean
    public CreateInvoicePaymentLinkUsecase createInvoicePaymentLinkUsecase(
            PaymentRepository paymentRepository,
            InvoicePaymentRepository invoicePaymentRepository,
            InvoiceRepository invoiceRepository,
            PaymentGateway payOSClient
    ) {
        return new CreateInvoicePaymentLinkUsecaseImpl(
                paymentRepository,
                invoicePaymentRepository,
                invoiceRepository,
                payOSClient
        );
    }

    @Bean
    public CreateInvoiceUsecase createInvoiceUsecase(
            InvoiceRepository invoiceRepository,
            InvoicePaymentRepository invoicePaymentRepository,
            PaymentRepository paymentRepository
    ) {
        return new CreateInvoiceUsecaseImpl(invoiceRepository, invoicePaymentRepository, paymentRepository);
    }

    @Bean
    public ProcessWebhookDataUseCase processWebhookDataUseCase(
            PaymentRepository paymentRepository,
            InvoiceRepository invoiceRepository,
            InvoicePaymentRepository invoicePaymentRepository
    ) {
        return new ProcessWebhookDataUseCaseImpl(
                paymentRepository,
                invoiceRepository,
                invoicePaymentRepository
        );
    }

    @Bean
    public AutoExpirePaymentsUsecase autoExpirePaymentUsecase(
            PaymentRepository paymentRepository,
            PaymentGateway payOSClient
    ) {
        return new AutoExpirePaymentUsecaseImpl(paymentRepository, payOSClient);
    }

    @Bean
    public RetrieveInvoiceUsecase retrieveInvoiceUsecase(InvoiceRepository invoiceRepository) {
        return new RetriveInvoiceUsecaseImpl(invoiceRepository);
    }

    @Bean
    public CreateDirectPaymentLinkUsecase createDirectPaymentLinkUsecase(
            InvoiceRepository invoiceRepository,
            PaymentRepository paymentRepository,
            InvoicePaymentRepository invoicePaymentRepository,
            PaymentGateway payOSClient
    ) {
        return new CreateDirectPaymentLinkUsecaseImpl(invoiceRepository, paymentRepository, invoicePaymentRepository, payOSClient);
    }

}
