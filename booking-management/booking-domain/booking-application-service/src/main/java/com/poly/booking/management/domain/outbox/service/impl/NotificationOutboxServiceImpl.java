package com.poly.booking.management.domain.outbox.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.booking.management.domain.exception.BookingDomainException;
import com.poly.booking.management.domain.outbox.payload.NotifiEventPayload;
import com.poly.booking.management.domain.outbox.model.NotifiOutboxMessage;
import com.poly.booking.management.domain.outbox.service.NotificationOutboxService;
import com.poly.booking.management.domain.port.out.repository.NotificationOutboxRepository;
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

/**
 * NotificationOutboxHelper - Helper Class for Notification Outbox Pattern
 * <p>
 * CHỨC NĂNG CHÍNH:
 * - Quản lý notification outbox messages trong Saga pattern
 * - Đảm bảo reliable message delivery cho notification service
 * - Cung cấp các utility methods để tương tác với notification outbox
 * <p>
 * ÁP DỤNG PATTERNS:
 * - Outbox Pattern: Đảm bảo message delivery reliability
 * - Saga Pattern: Track transaction state
 * - Repository Pattern: Data access abstraction
 * <p>
 * CÁC PHƯƠNG THỨC CHÍNH:
 * - getNotificationOutboxMessageBySagaIdAndSagaStatus: Tìm message theo saga ID và status
 * - save: Lưu notification outbox message
 * - saveNotificationOutboxMessage: Tạo và lưu notification outbox message
 * - getUpdatedNotificationOutBoxMessage: Cập nhật trạng thái message
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationOutboxServiceImpl implements NotificationOutboxService {

    private final NotificationOutboxRepository notificationOutboxRepository;
    private final ObjectMapper objectMapper;

    /**
     * Tìm notification outbox message theo saga ID và saga status
     *
     * @param sagaId   Saga identifier
     * @param statuses Các trạng thái saga cần tìm
     * @return Optional chứa notification outbox message nếu tìm thấy
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<NotifiOutboxMessage> getBySagaIdAndSagaStatus(UUID sagaId, SagaStatus... statuses) {
        return notificationOutboxRepository.findByTypeAndSagaIdAndSagaStatus(BOOKING_SAGA_NAME, sagaId, statuses);
    }

    @Override
    public Optional<List<NotifiOutboxMessage>> getListByBookingIdAndStatus(OutboxStatus outboxStatus, SagaStatus... sagaStatus) {
        return notificationOutboxRepository.findByTypeAndOutboxStatusAndSagaStatus(BOOKING_SAGA_NAME, outboxStatus, sagaStatus);
    }

    /**
     * Lưu notification outbox message vào database
     *
     * @param notificationOutboxMessage Message cần lưu
     */
    @Transactional
    public void save(NotifiOutboxMessage notificationOutboxMessage) {
        NotifiOutboxMessage response = notificationOutboxRepository.save(notificationOutboxMessage);
        if (response == null) {
            log.error("Could not save notification outbox message with id: {}", notificationOutboxMessage.getId());
            throw new BookingDomainException("Could not save NotificationOutboxMessage with id: " + notificationOutboxMessage.getId());
        }
        log.info("Saved notification outbox message with id: {}", notificationOutboxMessage.getId());
    }

    /**
     * Tạo và lưu notification outbox message
     *
     * @param notificationEventPayload Payload chứa notification data
     * @param bookingStatus            Trạng thái booking
     * @param sagaStatus               Trạng thái saga
     * @param outboxStatus             Trạng thái outbox
     * @param sagaId                   Saga identifier
     */
    @Transactional
    public void saveWithPayloadAndBookingStatusAndSagaStatusAndOutboxStatusAndSagaId
    (NotifiEventPayload notificationEventPayload,
     BookingStatus bookingStatus,
     SagaStatus sagaStatus,
     OutboxStatus outboxStatus,
     UUID sagaId) {
        save(NotifiOutboxMessage.builder()
                .id(UUID.randomUUID())
                .sagaId(sagaId)
                .type(BOOKING_SAGA_NAME)
                .sagaStatus(sagaStatus)
                .outboxStatus(outboxStatus)
                .payload(createPayload(notificationEventPayload))
                .bookingStatus(bookingStatus)
                .createdAt(notificationEventPayload.getCreatedAt())
                .bookingId(notificationEventPayload.getBookingId())
                .build());
        log.info("Notification outbox message has been created successfully!");
    }

    @Override
    public void deleteByOutboxStatusAndSagaStatus(OutboxStatus outboxStatus, SagaStatus... sagaStatus) {
        notificationOutboxRepository.deleteByTypeAndOutboxStatusAndSagaStatus(outboxStatus, sagaStatus);
        log.info("Deleted notification outbox message with outbox status: {} and saga status: {}", outboxStatus, sagaStatus);
    }

    /**
     * Cập nhật trạng thái notification outbox message
     *
     * @param notificationOutboxMessage Message cần cập nhật
     * @param bookingStatus             Trạng thái booking mới
     * @param sagaStatus                Trạng thái saga mới
     * @return Message đã được cập nhật
     */
    public NotifiOutboxMessage getUpdated(NotifiOutboxMessage notificationOutboxMessage,
                                          BookingStatus bookingStatus,
                                          SagaStatus sagaStatus) {
        notificationOutboxMessage.setBookingStatus(bookingStatus);
        notificationOutboxMessage.setSagaStatus(sagaStatus);
        notificationOutboxMessage.setProcessedAt(LocalDateTime.now());

        notificationOutboxMessage = notificationOutboxRepository.save(notificationOutboxMessage);
        log.info("Updated notification outbox message with id: {}", notificationOutboxMessage.getId());
        return notificationOutboxMessage;
    }

    /**
     * Tạo JSON payload từ notification event payload
     *
     * @param payload Notification event payload
     * @return JSON string representation
     */
    @Override
    public String createPayload(NotifiEventPayload payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (Exception e) {
            log.error("Could not create notification outbox message payload!", e);
            throw new BookingDomainException("Could not create notification outbox message payload!", e);
        }
    }

}
