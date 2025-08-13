package com.poly.booking.management.domain.outbox.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.booking.management.domain.exception.BookingDomainException;
import com.poly.booking.management.domain.outbox.payload.PaymentEventPayload;
import com.poly.booking.management.domain.outbox.model.PaymentOutboxMessage;
import com.poly.booking.management.domain.outbox.service.PaymentOutboxService;
import com.poly.booking.management.domain.port.out.repository.PaymentOutBoxRepository;
import com.poly.domain.valueobject.BookingStatus;
import com.poly.outbox.OutboxStatus;
import com.poly.saga.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.poly.saga.booking.SagaConstant.BOOKING_SAGA_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentOutboxImpl implements PaymentOutboxService {

    private final PaymentOutBoxRepository paymentOutBoxRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Optional<PaymentOutboxMessage> getBySagaIdAndSagaStatus(UUID sagaId, SagaStatus... sagaStatus) {
        return paymentOutBoxRepository.findByTypeAndSagaIdAndSagaStatus(BOOKING_SAGA_NAME, sagaId, sagaStatus);
    }

    @Override
    public Optional<List<PaymentOutboxMessage>> getListByBookingIdAndStatus(OutboxStatus outboxStatus, SagaStatus... sagaStatus) {
        return paymentOutBoxRepository.findByTypeAndOutboxStatusAndSagaStatus(BOOKING_SAGA_NAME, outboxStatus, sagaStatus);
    }

    @Override
    public void save(PaymentOutboxMessage updatePaymentOutboxMessage) {
        PaymentOutboxMessage response = paymentOutBoxRepository.save(updatePaymentOutboxMessage);
        if (response == null) {
            log.error("Could not save booking approval outbox message with id: {}", updatePaymentOutboxMessage.getId());
            throw new BookingDomainException("Could not save BookingApprovalOutboxMessage with id: " + updatePaymentOutboxMessage.getId());
        }
        log.info("Saved booking approval outbox message with id: {}", updatePaymentOutboxMessage.getId());
    }

    @Override
    public void saveWithPayloadAndBookingStatusAndSagaStatusAndOutboxStatusAndSagaId(PaymentEventPayload paymentEventPayload,
                                                                                     BookingStatus orderStatus,
                                                                                     SagaStatus sagaStatus,
                                                                                     OutboxStatus outboxStatus,
                                                                                     UUID sagaId) {
        save(PaymentOutboxMessage.builder()
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

    private String createPayload(PaymentEventPayload payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (Exception e) {
            log.error("Error when converting booking approval event payload to json string: {}", e.getMessage());
            throw new RuntimeException("Could not create BookingApprovalEventPayload with booking id: " + payload.getBookingId(), e);
        }
    }

    @Override
    public void deleteByOutboxStatusAndSagaStatus(OutboxStatus outboxStatus, SagaStatus... sagaStatus) {
        paymentOutBoxRepository.deleteByTypeAndOutboxStatusAndSagaStatus(BOOKING_SAGA_NAME, outboxStatus, sagaStatus);
        log.info("Deleted booking approval outbox message with outbox status: {} and saga status: {}", outboxStatus, sagaStatus);
    }
}
