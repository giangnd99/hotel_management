package com.poly.booking.management.domain.outbox.scheduler.room;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.booking.management.domain.event.BookingPaidEvent;
import com.poly.booking.management.domain.exception.BookingDomainException;
import com.poly.booking.management.domain.mapper.BookingDataMapper;
import com.poly.booking.management.domain.outbox.model.room.BookingReservedEventPayload;
import com.poly.booking.management.domain.outbox.model.room.BookingRoomOutboxMessage;
import com.poly.booking.management.domain.port.out.repository.RoomReserveOutBoxRepository;
import com.poly.domain.valueobject.EBookingStatus;
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
public class RoomOutboxHelper {

    private final RoomReserveOutBoxRepository roomReserveOutBoxRepository;
    private final BookingDataMapper bookingDataMapper;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public Optional<BookingRoomOutboxMessage> getApprovalOutboxMessageBySagaIdAndSagaStatus(UUID sagaId, SagaStatus... statuses) {
        return roomReserveOutBoxRepository.findByTypeAndSagaIdAndSagaStatus(BOOKING_SAGA_NAME, sagaId, statuses);
    }

    @Transactional(readOnly = true)
    public Optional<List<BookingRoomOutboxMessage>> getApprovalOutboxMessageByBookingIdAndStatus(OutboxStatus outboxStatus, SagaStatus... sagaStatus) {
        return roomReserveOutBoxRepository.findByTypeAndOutboxStatusAndSagaStatus(BOOKING_SAGA_NAME, outboxStatus, sagaStatus);
    }


    @Transactional
    public void save(BookingRoomOutboxMessage bookingRoomOutboxMessage) {
        BookingRoomOutboxMessage response = roomReserveOutBoxRepository.save(bookingRoomOutboxMessage);

        if (response.getId() == null) {
            log.error("Could not save booking approval outbox message with id: {}", bookingRoomOutboxMessage.getId());
            throw new BookingDomainException("Could not save BookingApprovalOutboxMessage with id: " + bookingRoomOutboxMessage.getId());
        }
        log.info("Saved booking approval outbox message with id: {}", bookingRoomOutboxMessage.getId());
    }

    public BookingRoomOutboxMessage getUpdatedRoomOutBoxMessage(BookingRoomOutboxMessage bookingRoomOutboxMessage, EBookingStatus status, SagaStatus sagaStatus) {
        bookingRoomOutboxMessage.setBookingStatus(status);
        bookingRoomOutboxMessage.setSagaStatus(sagaStatus);
        bookingRoomOutboxMessage.setProcessedAt(LocalDateTime.now());
        return bookingRoomOutboxMessage;
    }

    public BookingRoomOutboxMessage getConfirmedDepositOutboxMessage(BookingPaidEvent domainEvent,
                                                                     EBookingStatus status,
                                                                     SagaStatus sagaStatus,
                                                                     OutboxStatus outboxStatus,
                                                                     UUID sagaId) {
        BookingReservedEventPayload payload = bookingDataMapper.bookingEventToRoomBookingEventPayload(domainEvent);

        return BookingRoomOutboxMessage.builder()
                .id(UUID.randomUUID())
                .sagaId(sagaId)
                .type(BOOKING_SAGA_NAME)
                .bookingStatus(status)
                .sagaStatus(sagaStatus)
                .outboxStatus(outboxStatus)
                .payload(createPayload(payload))
                .createdAt(payload.getCreatedAt())
                .build();
    }

    private String createPayload(BookingReservedEventPayload payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            log.error("Error when converting booking approval event payload to json string: {}", e.getMessage());
            throw new BookingDomainException("Could not create BookingApprovalEventPayload with booking id: " + payload.getBookingId(), e);
        }
    }

    @Transactional
    public void deleteApprovalOutboxMessageByOutboxStatusAndSagaStatus(OutboxStatus outboxStatus,
                                                                       SagaStatus... sagaStatus) {
        roomReserveOutBoxRepository.deleteByTypeAndOutboxStatusAndSagaStatus(BOOKING_SAGA_NAME, outboxStatus, sagaStatus);
        log.info("Deleted booking approval outbox message with outbox status: {} and saga status: {}", outboxStatus, sagaStatus);
    }
}
