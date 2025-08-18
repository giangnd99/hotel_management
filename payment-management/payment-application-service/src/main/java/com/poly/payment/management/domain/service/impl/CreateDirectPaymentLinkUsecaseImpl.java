package com.poly.payment.management.domain.service.impl;

import com.poly.domain.valueobject.InvoiceId;
import com.poly.domain.valueobject.PaymentMethod;
import com.poly.domain.valueobject.PaymentStatus;
import com.poly.domain.valueobject.ReferenceId;
import com.poly.payment.management.domain.value_object.*;
import com.poly.payment.management.domain.dto.request.CreateDirectPaymentCommand;
import com.poly.payment.management.domain.dto.request.CreatePaymentLinkCommand;
import com.poly.payment.management.domain.dto.response.PaymentLinkResult;
import com.poly.payment.management.domain.exception.ApplicationServiceException;
import com.poly.payment.management.domain.port.input.service.CreateDirectPaymentLinkUsecase;
import com.poly.payment.management.domain.service.PaymentGateway;
import com.poly.payment.management.domain.dto.CheckoutResponseData;
import com.poly.payment.management.domain.dto.ItemData;
import com.poly.payment.management.domain.model.Invoice;
import com.poly.payment.management.domain.model.InvoicePayment;
import com.poly.payment.management.domain.model.Payment;
import com.poly.payment.management.domain.port.output.repository.InvoicePaymentRepository;
import com.poly.payment.management.domain.port.output.repository.InvoiceRepository;
import com.poly.payment.management.domain.port.output.repository.PaymentRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Optional;

public class CreateDirectPaymentLinkUsecaseImpl implements CreateDirectPaymentLinkUsecase {

    private final InvoiceRepository invoiceRepository;

    private final PaymentRepository paymentRepository;

    private final InvoicePaymentRepository invoicePaymentRepository;

    private final PaymentGateway payOSClient;

    public CreateDirectPaymentLinkUsecaseImpl(InvoiceRepository invoiceRepository, PaymentRepository paymentRepository, InvoicePaymentRepository invoicePaymentRepository, PaymentGateway payOSClient) {
        this.invoiceRepository = invoiceRepository;
        this.paymentRepository = paymentRepository;
        this.invoicePaymentRepository = invoicePaymentRepository;
        this.payOSClient = payOSClient;
    }

    @Override
    public PaymentLinkResult createPaymentLinkUseCase(CreateDirectPaymentCommand command) throws Exception {

        Optional<Payment> existedPayment = Optional.empty();

        if (command.getReferenceId() != null) {
            existedPayment = paymentRepository.findByReferenceId(command.getReferenceId());
        }

        if(existedPayment.isPresent()){
            if(existedPayment.get().getStatus().equals(PaymentStatus.PENDING)) {
                return PaymentLinkResult.builder()
                        .paymentId(existedPayment.get().getId().getValue())
                        .orderCode(existedPayment.get().getOrderCode().getValue())
                        .paymentLink(existedPayment.get().getPaymentLink())
                        .status(existedPayment.get().getStatus().name())
                        .build();
            }
            if (existedPayment.get().getStatus().equals(PaymentStatus.PAID)) {
                throw new ApplicationServiceException("Payment Deposit already exists");
            }
            if(existedPayment.get().getStatus().equals(PaymentStatus.CANCELED)) {
                throw new ApplicationServiceException("Payment Deposit canceled");
            }
        }

        Invoice invoice = Invoice.builder()
                .invoiceId(InvoiceId.generate())
                .invoiceStatus(InvoiceStatus.PENDING)
                .staffId(StaffId.from(command.getCustomerId() != null ? command.getCustomerId() : null))
                .customerId(CustomerId.fromValue(command.getCustomerId() != null ? command.getCustomerId() : null))
                .subTotal(Money.from(command.getSubTotal()))
                .taxRate(Money.from(command.getTaxRate() != null ? command.getTaxRate() : BigDecimal.ZERO))
                .totalAmount(calculateTotalAfterVoucherAndTax(command.getSubTotal(), command.getTaxRate(), command.getVoucherAmount()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .note(Description.from(command.getNotes() != null ? command.getNotes() : "empty"))
                .build();

        Payment payment = Payment.builder()
                .paymentId(PaymentId.generate())
                .status(PaymentStatus.PENDING)
                .referenceId(ReferenceId.from(command.getReferenceId() != null ? command.getReferenceId() : null))
                .method(PaymentMethod.PAYOS)
                .amount(invoice.getTotalAmount())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .orderCode(OrderCode.generate())
                .description(Description.from("service"))
                .build();

        InvoicePayment invoicePayment = InvoicePayment.builder()
                .id(InvoicePaymentId.generate())
                .paymentId(payment.getId())
                .paymentId(payment.getId())
                .build();

        // Tạo 1 đối tượng mà PayOS cần.
        CreatePaymentLinkCommand createPaymentLinkCommand = CreatePaymentLinkCommand.builder()
                .orderCode(payment.getOrderCode().getValue())
                .amount(BigDecimal.valueOf(payment.getAmount().getValue().setScale(0, RoundingMode.DOWN).longValue()))
                .items(command.getItems().stream().map(item -> ItemData.builder()
                        .name(item.getName())
                        .price(item.getPrice())
                        .quantity(item.getQuantity())
                        .build()).toList())
                .description("TTDV- " + payment.getOrderCode().getValue())
                .build();


        // Dữ liệu mà PayOS trả về khi tạo thành công hóa đơn
        CheckoutResponseData paymentLinkResult = payOSClient.createPaymentLink(createPaymentLinkCommand);

        // Map lại dữ liệu gồm link thanh toán, mã order, trạng thái
        PaymentLinkResult result = PaymentLinkResult.builder()
                .paymentId(payment.getId().getValue())
                .orderCode(createPaymentLinkCommand.getOrderCode())
                .status(paymentLinkResult.getStatus())
                .paymentLink(paymentLinkResult.getCheckoutUrl())
                .build();

        // Set lại link thanh toán payment
        payment.setPaymentLink(result.getPaymentLink());


        paymentRepository.save(payment);
        invoicePaymentRepository.save(invoicePayment);
        invoiceRepository.save(invoice);
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
