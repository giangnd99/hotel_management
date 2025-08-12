package com.poly.booking.management.domain.outbox.model;

import com.poly.domain.valueobject.EBookingStatus;
import com.poly.outbox.OutboxStatus;
import com.poly.saga.SagaStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class RoomOutboxMessage {
    private UUID id;
    private UUID sagaId;
    private String bookingId;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
    private String type;
    private String payload;
    private SagaStatus sagaStatus;
    private EBookingStatus bookingStatus;
    private OutboxStatus outboxStatus;
    private int version;

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public void setSagaStatus(SagaStatus sagaStatus) {
        this.sagaStatus = sagaStatus;
    }

    public void setBookingStatus(EBookingStatus bookingStatus) {
        this.bookingStatus = this.bookingStatus;
    }

    public void setOutboxStatus(OutboxStatus outboxStatus) {
        this.outboxStatus = outboxStatus;
    }
}
