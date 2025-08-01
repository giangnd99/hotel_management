package com.poly.booking.management.domain.outbox.model.payment;

import com.poly.domain.valueobject.DateCustom;
import com.poly.domain.valueobject.EBookingStatus;
import com.poly.domain.valueobject.OrderStatus;
import com.poly.outbox.OutboxStatus;
import com.poly.saga.SagaStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class BookingPaymentOutboxMessage {

    private UUID id;
    private UUID sagaId;
    private DateCustom createdAt;
    private DateCustom processedAt;
    private String type;
    private String payload;
    private SagaStatus sagaStatus;
    private EBookingStatus bookingStatus;
    private OutboxStatus outboxStatus;
    private int version;


    public void setProcessedAt(DateCustom processedAt) {
        this.processedAt = processedAt;
    }

    public void setSagaStatus(SagaStatus sagaStatus) {
        this.sagaStatus = sagaStatus;
    }

    public void setBookingStatus(EBookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public void setOutboxStatus(OutboxStatus outboxStatus) {
        this.outboxStatus = outboxStatus;
    }
}
