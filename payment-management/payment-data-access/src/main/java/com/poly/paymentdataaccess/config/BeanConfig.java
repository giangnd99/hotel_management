package com.poly.paymentdataaccess.config;


import com.poly.paymentapplicationservice.port.input.CreateDepositPaymentLinkUsecase;
import com.poly.paymentapplicationservice.port.input.CreateInvoicePaymentLinkUsecase;
import com.poly.paymentapplicationservice.port.input.CreateInvoiceUsecase;
import com.poly.paymentapplicationservice.port.input.ProcessWebhookDataUseCase;
import com.poly.paymentapplicationservice.port.output.PaymentGateway;
import com.poly.paymentapplicationservice.service.CreateDepositPaymentLinkUsecaseImpl;
import com.poly.paymentapplicationservice.service.CreateInvoicePaymentLinkUsecaseImpl;
import com.poly.paymentapplicationservice.service.CreateInvoiceUsecaseImpl;
import com.poly.paymentapplicationservice.service.ProcessWebhookDataUseCaseImpl;
import com.poly.paymentdomain.output.InvoicePaymentRepository;
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
}
