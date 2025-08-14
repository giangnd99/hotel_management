package com.poly.paymentapplicationservice.service;

import com.poly.domain.valueobject.InvoiceId;
import com.poly.paymentapplicationservice.dto.command.CreateInvoiceCommand;
import com.poly.paymentapplicationservice.dto.result.InvoiceResult;
import com.poly.paymentapplicationservice.exception.ApplicationServiceException;
import com.poly.paymentapplicationservice.port.input.CreateInvoiceUsecase;
import com.poly.paymentdomain.model.Invoice;
import com.poly.paymentdomain.model.InvoicePayment;
import com.poly.paymentdomain.model.Payment;
import com.poly.paymentdomain.model.value_object.CustomerId;
import com.poly.paymentdomain.model.value_object.InvoiceStatus;
import com.poly.paymentdomain.model.value_object.Money;
import com.poly.paymentdomain.model.value_object.StaffId;
import com.poly.paymentdomain.output.InvoicePaymentRepository;
import com.poly.paymentdomain.output.InvoiceRepository;
import com.poly.paymentdomain.output.PaymentRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public class CreateInvoiceUsecaseImpl implements CreateInvoiceUsecase {

    private final InvoiceRepository invoiceRepository;

    private final InvoicePaymentRepository invoicePaymentRepository;

    private final PaymentRepository paymentRepository;

    public CreateInvoiceUsecaseImpl(InvoiceRepository invoiceRepository, InvoicePaymentRepository invoicePaymentRepository, PaymentRepository paymentRepository) {
        this.invoiceRepository = invoiceRepository;
        this.invoicePaymentRepository = invoicePaymentRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public InvoiceResult createInvoice(CreateInvoiceCommand command) {
        Optional<Payment> exisingPayment = paymentRepository.findByReferenceId(command.getReferenceId());

        if(exisingPayment.isEmpty()){
            throw new ApplicationServiceException("Payment not found");
        }

        Optional<InvoicePayment> existingInvoicePayment = invoicePaymentRepository.findByPaymentId(exisingPayment.get().getId().getValue());
        if (existingInvoicePayment.isEmpty()){
            throw new ApplicationServiceException("InvoicePayment not found");
        }

        Invoice invoice = Invoice.builder()
                .invoiceId(InvoiceId.generate())
                .customerId(CustomerId.fromValue(command.getCustomerId()))
                .staffId(StaffId.from(command.getStaffId()))
                .subTotal(Money.from(command.getSubTotal()))
                .totalAmount(calculateTotalAmount(command.getSubTotal(), command.getTax() != null ?  command.getTax() : BigDecimal.ZERO))
                .taxRate(Money.from(command.getTax()))
                .invoiceStatus(InvoiceStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .note(command.getNote())
                .build();

        InvoicePayment invoicePayment = existingInvoicePayment.get();
        invoicePayment.setInvoiceId(invoice.getId());

        invoicePaymentRepository.save(invoicePayment);
        invoiceRepository.save(invoice);

        InvoiceResult result = InvoiceResult.builder()
                .invoiceId(invoice.getId().getValue())
                .customerId(invoice.getCustomerId().getValue())
                .staffId(invoice.getStaffId().getValue())
                .subAmount(invoice.getSubTotal().getValue())
                .totalAmount(invoice.getTotalAmount().getValue())
                .taxRate(invoice.getTaxRate().getValue())
                .status(invoice.getStatus().name())
                .createdAt(invoice.getCreatedAt())
                .updatedAt(invoice.getUpdatedAt())
                .note(invoice.getNote().getValue())
                .build();

        return result;
    }

    private Money calculateTotalAmount(BigDecimal subTotal, BigDecimal taxRate){
        BigDecimal taxAmount = taxRate.divide(BigDecimal.valueOf(100));
        BigDecimal totalAmount = subTotal.add(taxAmount);
        return Money.from(totalAmount);
    }
}
