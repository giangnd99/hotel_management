package com.poly.payment.management.domain.service.impl;

import com.poly.domain.valueobject.PaymentMethod;
import com.poly.domain.valueobject.PaymentStatus;
import com.poly.payment.management.domain.dto.request.CreateInvoicePaymentCommand;
import com.poly.payment.management.domain.dto.request.CreatePaymentLinkCommand;
import com.poly.payment.management.domain.dto.response.PaymentLinkResult;
import com.poly.payment.management.domain.exception.ApplicationServiceException;
import com.poly.payment.management.domain.port.input.service.CreateInvoicePaymentLinkUsecase;
import com.poly.payment.management.domain.service.PaymentGateway;
import com.poly.payment.management.domain.dto.CheckoutResponseData;
import com.poly.payment.management.domain.dto.ItemData;
import com.poly.payment.management.domain.model.Invoice;
import com.poly.payment.management.domain.model.InvoicePayment;
import com.poly.payment.management.domain.model.Payment;
import com.poly.payment.management.domain.value_object.Description;
import com.poly.payment.management.domain.value_object.Money;
import com.poly.payment.management.domain.value_object.OrderCode;
import com.poly.payment.management.domain.value_object.PaymentId;
import com.poly.payment.management.domain.port.output.repository.InvoicePaymentRepository;
import com.poly.payment.management.domain.port.output.repository.InvoiceRepository;
import com.poly.payment.management.domain.port.output.repository.PaymentRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class CreateInvoicePaymentLinkUsecaseImpl implements CreateInvoicePaymentLinkUsecase {

    private final PaymentRepository paymentRepository;

    private final InvoicePaymentRepository invoicePaymentRepository;

    private final InvoiceRepository invoiceRepository;

    private final PaymentGateway payOSClient;

    public CreateInvoicePaymentLinkUsecaseImpl(PaymentRepository paymentRepository, InvoicePaymentRepository invoicePaymentRepository, InvoiceRepository invoiceRepository, PaymentGateway payOSClient) {
        this.paymentRepository = paymentRepository;
        this.invoicePaymentRepository = invoicePaymentRepository;
        this.invoiceRepository = invoiceRepository;
        this.payOSClient = payOSClient;
    }

    @Override
    public PaymentLinkResult createPaymentLinkUseCase(CreateInvoicePaymentCommand command) throws Exception {
        Optional<Invoice> existedInvoice = invoiceRepository.findById(command.getInvoiceId());

        if (existedInvoice.isEmpty()) throw new Exception("Invoice not found");

        if (existedInvoice.isPresent() && existedInvoice.get().getStatus().equals(PaymentStatus.PAID)) throw new Exception("Payment already exists");

        Optional<InvoicePayment> existedInvoicePayment = invoicePaymentRepository.findByInvoiceId(existedInvoice.get().getId().getValue());

        if (existedInvoicePayment.isEmpty()) throw new Exception("Invoice not found");

        Invoice invoice = existedInvoice.get();

        InvoicePayment invoicePayment = existedInvoicePayment.get();

        Payment payment = Payment.builder()
                .paymentId(PaymentId.generate())
                .status(PaymentStatus.PENDING)
                .amount(invoice.getTotalAmount())
                .method(PaymentMethod.valueOf(command.getMethod() != null ? command.getMethod() : PaymentMethod.PAYOS.name()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .orderCode(OrderCode.generate())
                .description(Description.from("invoice_payment"))
                .build();

        InvoicePayment newInvoicePayment = InvoicePayment.builder()
                .paymentId(payment.getId())
                .invoiceId(invoice.getId())
                .build();

        // Tạo 1 đối tượng mà PayOS cần.
        CreatePaymentLinkCommand createPaymentLinkCommand = CreatePaymentLinkCommand.builder()
                .orderCode(payment.getOrderCode().getValue())
                .amount(BigDecimal.valueOf(payment.getAmount().getValue().setScale(0, RoundingMode.DOWN).longValue()))
                .items(List.of(ItemData.builder()
                                .price(payment.getAmount().getValue())
                                .name("Hóa đơn: " + payment.getOrderCode().getValue())
                                .quantity(1)
                        .build()))
                .description("TTHD- " + payment.getOrderCode().getValue())
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

        // Lưu lại payment vào cuối để tránh lỗi từ PayOS
        invoicePaymentRepository.save(newInvoicePayment);
        paymentRepository.save(payment);

        // Result trả về: id payment, mã orderCode, link thanh toán , trạng thái
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
