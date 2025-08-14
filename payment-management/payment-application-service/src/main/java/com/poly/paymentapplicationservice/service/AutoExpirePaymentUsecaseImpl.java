package com.poly.paymentapplicationservice.service;

import com.poly.domain.valueobject.PaymentStatus;
import com.poly.paymentapplicationservice.port.input.AutoExpirePaymentsUsecase;
import com.poly.paymentapplicationservice.port.output.PaymentGateway;
import com.poly.paymentdomain.model.Payment;
import com.poly.paymentdomain.output.PaymentRepository;

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
