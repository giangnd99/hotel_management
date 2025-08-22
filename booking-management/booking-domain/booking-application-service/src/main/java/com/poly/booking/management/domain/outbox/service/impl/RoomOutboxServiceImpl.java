package com.poly.booking.management.domain.outbox.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.booking.management.domain.exception.BookingDomainException;
import com.poly.booking.management.domain.outbox.payload.ReservedEventPayload;
import com.poly.booking.management.domain.outbox.model.RoomOutboxMessage;
import com.poly.booking.management.domain.outbox.service.RoomOutboxService;
import com.poly.booking.management.domain.port.out.repository.RoomReserveOutBoxRepository;
import com.poly.domain.valueobject.BookingStatus;
import com.poly.outbox.OutboxStatus;
import com.poly.saga.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.poly.saga.booking.SagaConstant.BOOKING_SAGA_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoomOutboxServiceImpl implements RoomOutboxService {

    private final RoomReserveOutBoxRepository roomReserveOutBoxRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<RoomOutboxMessage> getRoomOutboxMessageBySagaIdAndSagaStatus(UUID sagaId, SagaStatus... statuses) {
        return roomReserveOutBoxRepository.findByTypeAndSagaIdAndSagaStatus(BOOKING_SAGA_NAME, sagaId, statuses);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<List<RoomOutboxMessage>> getRoomOutboxMessagesBySagaIdAndSagaStatus(UUID sagaId, SagaStatus... statuses) {
        return roomReserveOutBoxRepository.findAllByTypeAndSagaIdAndSagaStatus(BOOKING_SAGA_NAME, sagaId, statuses);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<List<RoomOutboxMessage>> getRoomOutboxMessageByBookingIdAndStatus(OutboxStatus outboxStatus, SagaStatus... sagaStatus) {
        return roomReserveOutBoxRepository.findByTypeAndOutboxStatusAndSagaStatus(BOOKING_SAGA_NAME, outboxStatus, sagaStatus);
    }

    @Override
    @Transactional
    public void save(RoomOutboxMessage roomOutboxMessage) {
        RoomOutboxMessage response = roomReserveOutBoxRepository.save(roomOutboxMessage);

        if (response.getId() == null) {
            log.error("Could not save booking approval outbox message with id: {}", roomOutboxMessage.getId());
            throw new BookingDomainException("Could not save BookingApprovalOutboxMessage with id: " + roomOutboxMessage.getId());
        }
        log.info("Saved booking approval outbox message with id: {}", roomOutboxMessage.getId());
    }

    @Override
    @Transactional
    public RoomOutboxMessage getUpdatedRoomOutBoxMessage(RoomOutboxMessage roomOutboxMessage,
                                                         BookingStatus status,
                                                         SagaStatus sagaStatus) {
        roomOutboxMessage.setBookingStatus(status);
        roomOutboxMessage.setSagaStatus(sagaStatus);
        roomOutboxMessage.setProcessedAt(LocalDateTime.now());

        roomOutboxMessage = roomReserveOutBoxRepository.save(roomOutboxMessage);
        log.info("Updated booking approval outbox message with payload: {}", roomOutboxMessage.getPayload());
        log.info("Updated booking approval outbox message with id: {}", roomOutboxMessage.getId());
        return roomOutboxMessage;
    }

    private String createPayload(ReservedEventPayload payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            log.error("Error when converting booking approval event payload to json string: {}", e.getMessage());
            throw new BookingDomainException("Could not create BookingApprovalEventPayload with booking id: " + payload.getBookingId(), e);
        }
    }

    @Override
    @Transactional
    public void deleteRoomOutboxMessageByOutboxStatusAndSagaStatus(OutboxStatus outboxStatus,
                                                                   SagaStatus... sagaStatus) {
        roomReserveOutBoxRepository.deleteByTypeAndOutboxStatusAndSagaStatus(BOOKING_SAGA_NAME, outboxStatus, sagaStatus);
        log.info("Deleted booking approval outbox message with outbox status: {} and saga status: {}", outboxStatus, sagaStatus);
    }

    @Override
    @Transactional
    public void saveRoomOutboxMessage(ReservedEventPayload reservedEventPayload, BookingStatus status, SagaStatus sagaStatus, OutboxStatus outboxStatus, UUID uuid) {

        save(RoomOutboxMessage.builder()
                .id(UUID.randomUUID())
                .sagaId(uuid)
                .type(BOOKING_SAGA_NAME)
                .bookingId(UUID.fromString(reservedEventPayload.getBookingId()))
                .sagaStatus(sagaStatus)
                .outboxStatus(outboxStatus)
                .payload(createPayload(reservedEventPayload))
                .bookingStatus(status)
                .createdAt(reservedEventPayload.getCreatedAt())
                .build());
    }
}
