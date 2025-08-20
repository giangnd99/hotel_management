package com.poly.payment.management.domain.schedule.impl;

import com.poly.payment.management.domain.model.Payment;
import com.poly.payment.management.domain.port.output.repository.PaymentRepository;
import com.poly.payment.management.domain.schedule.PaymentExpiredSchedule;
import com.poly.payment.management.domain.service.PaymentGateway;
import com.poly.payment.management.domain.value_object.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Log4j2
@RequiredArgsConstructor
public class PaymentSchedule implements PaymentExpiredSchedule {
    private final PaymentRepository paymentRepository;

    private final PaymentGateway payOSClient;

    @Override
    @Scheduled(fixedRate = 60000, initialDelay = 10000)
    public void paymentExpiredSchedule() throws Exception {
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);

        List<Payment> expiredPayments = paymentRepository
                .findAllByStatusAndCreatedAtBefore(PaymentStatus.PENDING, tenMinutesAgo);

        for (Payment payment : expiredPayments) {
            payment.markAsExpired();
            payOSClient.cancelPaymentLink(payment.getOrderCode().getValue(), "expired");
            paymentRepository.save(payment);
        }

        log.info("PayOs Payment Gateway has been canceled");
    }
}
