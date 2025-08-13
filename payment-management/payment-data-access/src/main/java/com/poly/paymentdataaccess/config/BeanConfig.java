package com.poly.paymentdataaccess.config;

import com.poly.paymentapplicationservice.port.input.ok.*;
import com.poly.paymentapplicationservice.port.output.PaymentGateway;
import com.poly.paymentapplicationservice.service.ok.*;
import com.poly.paymentdomain.model.entity.InvoiceBooking;
import com.poly.paymentdomain.output.*;
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
    public AddRestaurantChargeUseCase addRestaurantChargeUseCase(
            InvoiceBookingRepository invoiceBookingRepository,
            InvoiceRestaurantRepository invoiceRestaurantRepository
    ) {
        return new AddRestaurantChargeUseCaseImpl(invoiceBookingRepository, invoiceRestaurantRepository);
    }

    @Bean
    public AddServiceChargeUseCase addServiceChargeUseCase(
            InvoiceBookingRepository invoiceBookingRepository,
            InvoiceServiceRepository invoiceServiceRepository
    ) {
        return new AddServiceChargeUseCaseImpl(invoiceBookingRepository, invoiceServiceRepository);
    }

    @Bean
    public CreateFinalInvoiceUseCase createFinalInvoiceUseCase(
            InvoiceRepository invoiceRepository,
            InvoiceBookingRepository invoiceBookingRepositorym,
            InvoiceRestaurantRepository invoiceRestaurantRepository,
            InvoiceVoucherRepository invoiceVoucherRepository,
            InvoiceServiceRepository invoiceServiceRepository,
            InvoicePaymentRepository invoicePaymentRepository,
            PaymentRepository paymentRepository
    ) {
        return new CreateFinalInvoiceUseCaseImpl(invoiceRepository, invoiceBookingRepositorym, invoiceRestaurantRepository,  invoiceVoucherRepository, invoiceServiceRepository, invoicePaymentRepository, paymentRepository);
    }

    @Bean
    public DepositPaymentLinkUseCase depositPaymentLinkUseCase(
            PaymentRepository paymentRepository,
            InvoiceBookingRepository invoiceBookingRepository,
            PaymentGateway payOSClient
    ) {
        return new DepositPaymentLinkUseCaseImpl(paymentRepository, invoiceBookingRepository, payOSClient);
    }

    @Bean
    public InvoicePaymentLinkUseCase invoicePaymentLinkUseCase(
            InvoiceRepository invoiceRepository,
            PaymentRepository paymentRepository,
            InvoicePaymentRepository invoicePaymentRepository,
            InvoiceRestaurantRepository invoiceRestaurantRepository,
            InvoiceBookingRepository invoiceBookingRepository,
            InvoiceServiceRepository invoiceServiceRepository,
            InvoiceVoucherRepository invoiceVoucherRepository,
            PaymentGateway payOSClient
    ) {
        return new InvoicePaymentLinkUseCaseImpl(
                invoiceRepository,
                paymentRepository,
                invoicePaymentRepository,
                invoiceRestaurantRepository,
                invoiceBookingRepository,
                invoiceServiceRepository,
                invoiceVoucherRepository,
                payOSClient);
    }

    @Bean
    public ProcessDirectPaymentUseCase processDirectPaymentUseCase(
            InvoiceRepository invoiceRepository,
            PaymentRepository paymentRepository,
            InvoiceRestaurantRepository invoiceRestaurantRepository,
            InvoicePaymentRepository invoicePaymentRepository,
            InvoiceServiceRepository invoiceServiceRepository,
            PaymentGateway payOSClient
    ) {
        return new ProcessDirectPaymentUseCaseImpl(
                invoiceRepository,
                paymentRepository,
                invoiceRestaurantRepository,
                invoicePaymentRepository,
                invoiceServiceRepository,
                payOSClient
        );
    }
}
