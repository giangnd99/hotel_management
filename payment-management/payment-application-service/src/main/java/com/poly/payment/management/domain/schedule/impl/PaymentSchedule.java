package com.poly.payment.management.domain.schedule.impl;

import com.poly.payment.management.domain.message.BookingPaymentResponse;
import com.poly.payment.management.domain.model.Payment;
import com.poly.payment.management.domain.port.output.publisher.BookingPaymentCheckoutReplyPublisher;
import com.poly.payment.management.domain.port.output.publisher.BookingPaymentReplyPublisher;
import com.poly.payment.management.domain.port.output.repository.PaymentRepository;
import com.poly.payment.management.domain.schedule.PaymentExpiredSchedule;
import com.poly.payment.management.domain.service.PaymentGateway;
import com.poly.payment.management.domain.value_object.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Component
@Log4j2
@RequiredArgsConstructor
public class PaymentSchedule implements PaymentExpiredSchedule {
    private final PaymentRepository paymentRepository;

    private final PaymentGateway payOSClient;
    private final BookingPaymentReplyPublisher bookingPaymentReplyPublisher;
    private final BookingPaymentCheckoutReplyPublisher bookingPaymentReplyCPublisher;
    private Set<String> paymentMap = new HashSet<>();
    private Set<String> paymentCheckoutMap = new HashSet<>();

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

    @Scheduled(fixedRate = 500, initialDelay = 10000)
    @Transactional
    public void paymentDepositExpiredSchedule() throws Exception {
        List<Payment> depositedPayment = paymentRepository.findAll();
        depositedPayment.forEach(payment -> {
            if (payment.getDescription().getValue().equalsIgnoreCase("deposit")
                    && !paymentMap.contains(payment.getId().getValue().toString())
                    && payment.getStatus().equals(PaymentStatus.PAID)
            ) {
                log.info("Payment {}: {} description {}", payment.getId().getValue(), payment.getStatus(), payment.getDescription().getValue());
                BookingPaymentResponse message = createBookingPaymentResponse(payment);
                log.info("Payment {}: {}", payment.getId().getValue(), message);
                bookingPaymentReplyPublisher.publishBookingPaymentReply(message);
                paymentMap.add(payment.getId().getValue().toString());
                log.info("Payment {}: published", payment.getId().getValue());
            }
        });
    }
    @Scheduled(fixedRate = 500, initialDelay = 10000)
    @Transactional
    public void paymentCheckOutExpiredSchedule() throws Exception {
        List<Payment> depositedPayment = paymentRepository.findAll();
        depositedPayment.forEach(payment -> {
            if (payment.getDescription().getValue().equalsIgnoreCase("checkout")
                    && !paymentCheckoutMap.contains(payment.getId().getValue().toString())
                    && payment.getStatus().equals(PaymentStatus.PAID)
            ) {
                log.info("Payment {}: {} description {}", payment.getId().getValue(), payment.getStatus(), payment.getDescription().getValue());
                BookingPaymentResponse message = createBookingPaymentResponse(payment);
                log.info("Payment {}: {}", payment.getId().getValue(), message);
                bookingPaymentReplyCPublisher.publish(message);
                paymentCheckoutMap.add(payment.getId().getValue().toString());
                log.info("Payment {}: published", payment.getId().getValue());
            }
        });
    }



    private BookingPaymentResponse createBookingPaymentResponse(Payment payment) {
        return BookingPaymentResponse.builder()
                .paymentId(payment.getId().getValue().toString())
                .price(payment.getAmount().getValue())
                .bookingId(
                        payment.getReferenceId().getValue().toString() != null ? payment.getReferenceId().getValue().toString() : null
                )
                .id(UUID.randomUUID().toString())
                .createdAt(payment.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant())
                .failureMessages(List.of(payment.getStatus().name()))
                .paymentStatus(exchangePaymentStatus(payment.getStatus()))
                .sagaId(UUID.randomUUID().toString())
                .build();
    }

    private com.poly.payment.management.domain.message.PaymentStatus exchangePaymentStatus(PaymentStatus paymentStatus) {
        return paymentStatus == PaymentStatus.PAID ?
                com.poly.payment.management.domain.message.PaymentStatus.COMPLETED :
                com.poly.payment.management.domain.message.PaymentStatus.FAILED;
    }
}
