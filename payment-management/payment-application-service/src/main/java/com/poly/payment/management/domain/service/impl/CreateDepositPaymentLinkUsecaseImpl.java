package com.poly.payment.management.domain.service.impl;

import com.poly.domain.valueobject.PaymentMethod;
import com.poly.domain.valueobject.PaymentStatus;
import com.poly.domain.valueobject.ReferenceId;
import com.poly.payment.management.domain.value_object.*;
import com.poly.payment.management.domain.dto.request.CreatePaymentDepositCommand;
import com.poly.payment.management.domain.dto.request.CreatePaymentLinkCommand;
import com.poly.payment.management.domain.dto.response.PaymentLinkResult;
import com.poly.payment.management.domain.exception.ApplicationServiceException;
import com.poly.payment.management.domain.port.input.service.CreateDepositPaymentLinkUsecase;
import com.poly.payment.management.domain.service.PaymentGateway;
import com.poly.payment.management.domain.dto.CheckoutResponseData;
import com.poly.payment.management.domain.dto.ItemData;
import com.poly.payment.management.domain.model.InvoicePayment;
import com.poly.payment.management.domain.model.Payment;
import com.poly.payment.management.domain.port.output.repository.InvoicePaymentRepository;
import com.poly.payment.management.domain.port.output.repository.PaymentRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public class CreateDepositPaymentLinkUsecaseImpl implements CreateDepositPaymentLinkUsecase {

    private final PaymentRepository paymentRepository;

    private final InvoicePaymentRepository invoicePaymentRepository;

    private final PaymentGateway payOSClient;

    public CreateDepositPaymentLinkUsecaseImpl(PaymentRepository paymentRepository, InvoicePaymentRepository invoicePaymentRepository, PaymentGateway payOSClient) {
        this.paymentRepository = paymentRepository;
        this.invoicePaymentRepository = invoicePaymentRepository;
        this.payOSClient = payOSClient;
    }

    @Override
    public PaymentLinkResult createPaymentLinkUseCase(CreatePaymentDepositCommand command) throws Exception {

        Optional<Payment> existedPayment = paymentRepository.findByReferenceId(command.getReferenceId());

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

        Payment payment = Payment.builder()
                .paymentId(PaymentId.generate())
                .referenceId(ReferenceId.from(command.getReferenceId()))
                .status(PaymentStatus.PENDING)
                .amount(Money.from(command.getAmount()))
                .method(PaymentMethod.valueOf(command.getMethod() != null ? command.getMethod() : PaymentMethod.PAYOS.name()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .orderCode(OrderCode.generate())
                .description(Description.from("deposit"))
                .build();

        InvoicePayment invoicePayment = InvoicePayment.builder()
                .id(InvoicePaymentId.generate())
                .paymentId(payment.getId())
                .build();

        // Tạo 1 đối tượng mà PayOS cần.
        CreatePaymentLinkCommand createPaymentLinkCommand = CreatePaymentLinkCommand.builder()
                .orderCode(payment.getOrderCode().getValue())
                .amount(command.getAmount())
                .items(command.getItems().stream().map(item -> ItemData.builder()
                        .name(item.getName())
                        .price(item.getPrice())
                        .quantity(item.getQuantity())
                        .build()).toList())
                .description("TTDC- " + payment.getOrderCode().getValue())
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
        invoicePaymentRepository.save(invoicePayment);
        paymentRepository.save(payment);
//publish(message)
        // Result trả về: id payment, mã orderCode, link thanh toán , trạng thái
        return result;
    }
}
