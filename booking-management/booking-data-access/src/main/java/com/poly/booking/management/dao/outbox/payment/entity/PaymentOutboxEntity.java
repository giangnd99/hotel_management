package com.poly.booking.management.dao.outbox.payment.entity;

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
@Table(name = "payment_outbox")
@Entity
public class PaymentOutboxEntity {

    @Id
    private UUID id;
    private UUID sagaId;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
    private String type;
    private String payload;
    private String bookingId;
    @Enumerated(EnumType.STRING)
    private SagaStatus sagaStatus;
    @Enumerated(EnumType.STRING)
    private EBookingStatus bookingStatus;
    @Enumerated(EnumType.STRING)
    private OutboxStatus outboxStatus;
    @Version
    private int version;
}
