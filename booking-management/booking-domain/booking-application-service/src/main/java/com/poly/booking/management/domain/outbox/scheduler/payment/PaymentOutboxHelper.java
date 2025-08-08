package com.poly.booking.management.domain.outbox.scheduler.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.booking.management.domain.exception.BookingDomainException;
import com.poly.booking.management.domain.outbox.model.payment.BookingPaymentEventPayload;
import com.poly.booking.management.domain.outbox.model.payment.BookingPaymentOutboxMessage;
import com.poly.booking.management.domain.port.out.repository.PaymentOutBoxRepository;
import com.poly.domain.valueobject.DateCustom;
import com.poly.domain.valueobject.EBookingStatus;
import com.poly.outbox.OutboxStatus;
import com.poly.saga.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static com.poly.saga.booking.SagaConstant.BOOKING_SAGA_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentOutboxHelper {

    private final PaymentOutBoxRepository paymentOutBoxRepository;
    private final ObjectMapper objectMapper;

    public Optional<BookingPaymentOutboxMessage> getPaymentOutboxMessageBySagaIdAndSagaStatus(UUID uuid, SagaStatus... statuses) {
        return Optional.empty();
    }

    public void save(BookingPaymentOutboxMessage updatePaymentOutboxMessage) {
        BookingPaymentOutboxMessage response = paymentOutBoxRepository.save(updatePaymentOutboxMessage);
        if (response == null) {
            log.error("Could not save booking approval outbox message with id: {}", updatePaymentOutboxMessage.getId());
            throw new BookingDomainException("Could not save BookingApprovalOutboxMessage with id: " + updatePaymentOutboxMessage.getId());
        }
        log.info("Saved booking approval outbox message with id: {}", updatePaymentOutboxMessage.getId());
    }


    public void savePaymentOutboxMessage(BookingPaymentEventPayload paymentEventPayload,
                                         EBookingStatus orderStatus,
                                         SagaStatus sagaStatus,
                                         OutboxStatus outboxStatus,
                                         UUID sagaId) {
        save(BookingPaymentOutboxMessage.builder()
                .id(UUID.randomUUID())
                .sagaId(sagaId)
                .type(BOOKING_SAGA_NAME)
                .sagaStatus(sagaStatus)
                .outboxStatus(outboxStatus)
                .payload(createPayload(paymentEventPayload))
                .bookingStatus(orderStatus)
                .createdAt(paymentEventPayload.getCreatedAt())
                .build());
        log.info("Outbox message has been created successfully! ");
    }

    private String createPayload(BookingPaymentEventPayload payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (Exception e) {
            log.error("Error when converting booking approval event payload to json string: {}", e.getMessage());
            throw new RuntimeException("Could not create BookingApprovalEventPayload with booking id: " + payload.getBookingId(), e);
        }
    }

    public void deletePaymentOutboxMessageByOutboxStatusAndSagaStatus(OutboxStatus outboxStatus, SagaStatus... sagaStatus) {
        paymentOutBoxRepository.deleteByTypeAndOutboxStatusAndSagaStatus(BOOKING_SAGA_NAME,outboxStatus,sagaStatus);
    }
}
