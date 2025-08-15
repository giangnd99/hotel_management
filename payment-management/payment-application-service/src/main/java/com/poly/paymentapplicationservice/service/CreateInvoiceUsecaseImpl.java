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
import java.math.RoundingMode;
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
                .totalAmount(calculateTotalAfterVoucherAndTax(command.getSubTotal(), command.getTax(),  command.getVoucherAmount()))
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

    private Money calculateTotalAfterVoucherAndTax(BigDecimal subTotal, BigDecimal taxRate, BigDecimal voucherAmount) {
        if (subTotal == null) {
            throw new ApplicationServiceException("SubTotal not found");
        }

        // 1. Tính số tiền giảm giá
        BigDecimal discount = BigDecimal.ZERO;
        if (voucherAmount != null && voucherAmount.compareTo(BigDecimal.ZERO) > 0) {
            if (voucherAmount.compareTo(BigDecimal.ONE) >= 0 &&
                    voucherAmount.compareTo(new BigDecimal("99")) <= 0) {
                // voucherAmount từ 1 → 100 => % giảm
                discount = subTotal.multiply(voucherAmount)
                        .divide(new BigDecimal("99"), 2, RoundingMode.HALF_UP);
            } else {
                // Giảm thẳng số tiền
                discount = voucherAmount;
            }
        }

        // 2. Trừ voucher vào subtotal
        BigDecimal discountedTotal = subTotal.subtract(discount);

        // Không để âm (phòng trường hợp voucher > subtotal)
        if (discountedTotal.compareTo(BigDecimal.ZERO) < 0) {
            discountedTotal = BigDecimal.ZERO;
        }

        // 3. Tính thuế
        BigDecimal taxAmount = BigDecimal.ZERO;
        if (taxRate != null && taxRate.compareTo(BigDecimal.ZERO) > 0) {
            taxAmount = discountedTotal.multiply(taxRate)
                    .divide(new BigDecimal("99"), 2, RoundingMode.HALF_UP);
        }

        // 4. Tổng cuối cùng
        BigDecimal finalTotal = discountedTotal.add(taxAmount);

        return Money.from(finalTotal);
    }
}
