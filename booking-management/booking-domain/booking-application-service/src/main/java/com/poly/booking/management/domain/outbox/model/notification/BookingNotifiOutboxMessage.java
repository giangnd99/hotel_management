package com.poly.booking.management.domain.outbox.model.notification;

import com.poly.domain.valueobject.EBookingStatus;
import com.poly.outbox.OutboxStatus;
import com.poly.saga.SagaStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * BookingNotifiOutboxMessage - Outbox Message Model for Notification Processing
 * 
 * CHỨC NĂNG:
 * - Lưu trữ notification messages trong outbox pattern
 * - Đảm bảo reliable message delivery cho notification service
 * - Track trạng thái xử lý notification trong Saga flow
 * 
 * ÁP DỤNG PATTERNS:
 * - Outbox Pattern: Đảm bảo message delivery reliability
 * - Saga Pattern: Track transaction state
 * 
 * CÁC TRƯỜNG DỮ LIỆU:
 * - id: Unique identifier cho outbox message
 * - sagaId: Saga identifier để track transaction
 * - type: Loại saga (BOOKING_SAGA_NAME)
 * - sagaStatus: Trạng thái saga hiện tại
 * - outboxStatus: Trạng thái outbox message
 * - payload: JSON payload chứa notification data
 * - bookingStatus: Trạng thái booking
 * - createdAt: Thời gian tạo message
 * - processedAt: Thời gian xử lý message
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class BookingNotifiOutboxMessage {
    private UUID id;
    private UUID sagaId;
    private String type;
    private String bookingId;
    private SagaStatus sagaStatus;
    private OutboxStatus outboxStatus;
    private String payload;
    private EBookingStatus bookingStatus;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
}
