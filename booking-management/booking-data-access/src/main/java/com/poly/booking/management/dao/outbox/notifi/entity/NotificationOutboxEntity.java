package com.poly.booking.management.dao.outbox.notifi.entity;

import com.poly.domain.valueobject.EBookingStatus;
import com.poly.outbox.OutboxStatus;
import com.poly.saga.SagaStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notification_outbox")
@Entity
public class NotificationOutboxEntity {

    @Id
    private UUID id;
    private UUID sagaId;
    private String type;
    private String bookingId;
    @Enumerated(EnumType.STRING)
    private SagaStatus sagaStatus;
    @Enumerated(EnumType.STRING)
    private OutboxStatus outboxStatus;
    private String payload;
    @Enumerated(EnumType.STRING)
    private EBookingStatus bookingStatus;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
    @Version
    private int version;
}
