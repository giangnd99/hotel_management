package com.poly.payment.management.domain.service.impl;

import com.poly.domain.valueobject.PaymentStatus;
import com.poly.payment.management.domain.port.input.service.AutoExpirePaymentsUsecase;
import com.poly.payment.management.domain.service.PaymentGateway;
import com.poly.payment.management.domain.model.Payment;
import com.poly.payment.management.domain.port.output.repository.PaymentRepository;

import java.time.LocalDateTime;
import java.util.List;

public class AutoExpirePaymentUsecaseImpl implements AutoExpirePaymentsUsecase {

    private final PaymentRepository paymentRepository;

    private final PaymentGateway payOSClient;

    public AutoExpirePaymentUsecaseImpl(PaymentRepository paymentRepository, PaymentGateway payOSClient) {
        this.paymentRepository = paymentRepository;
        this.payOSClient = payOSClient;
    }

    @Override
    public void execute() throws Exception {
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);

        List<Payment> expiredPayments = paymentRepository
                .findAllByStatusAndCreatedAtBefore(PaymentStatus.PENDING, tenMinutesAgo);

        for (Payment payment : expiredPayments) {
            payment.markAsExpired();
            payOSClient.cancelPaymentLink(payment.getOrderCode().getValue(), "expired");
            paymentRepository.save(payment);
        }
    }
}
