package com.poly.booking.management.dao.outbox.room.entity;

import com.poly.domain.valueobject.EBookingStatus;
import com.poly.outbox.OutboxStatus;
import com.poly.saga.SagaStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "room_outbox")
@Entity
public class RoomOutboxEntity {
    @Id
    private UUID id;
    private UUID sagaId;
    private String bookingId;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
    private String type;
    private String payload;
    @Enumerated(EnumType.STRING)
    private SagaStatus sagaStatus;
    @Enumerated(EnumType.STRING)
    private EBookingStatus bookingStatus;
    @Enumerated(EnumType.STRING)
    private OutboxStatus outboxStatus;
    @Version
    private int version;
}
