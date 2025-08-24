package com.poly.booking.management.dao.outbox.room.mapper;

import com.poly.booking.management.dao.outbox.room.entity.RoomOutboxEntity;
import com.poly.booking.management.domain.outbox.model.RoomOutboxMessage;
import org.springframework.stereotype.Component;

@Component
public class RoomOutboxDataMapper {

    public RoomOutboxMessage toModel(RoomOutboxEntity entity) {
        return RoomOutboxMessage.builder()
                .id(entity.getId())
                .type(entity.getType())
                .bookingId(entity.getBookingId())
                .outboxStatus(entity.getOutboxStatus())
                .bookingStatus(entity.getBookingStatus())
                .sagaId(entity.getSagaId())
                .sagaStatus(entity.getSagaStatus())
                .payload(entity.getPayload())
                .createdAt(entity.getCreatedAt())
                .processedAt(entity.getProcessedAt())
                .version(entity.getVersion())
                .build();
    }
    public RoomOutboxEntity toEntity(RoomOutboxMessage model){
        return RoomOutboxEntity.builder()
                .id(model.getId())
                .type(model.getType())
                .bookingId(model.getBookingId())
                .bookingStatus(model.getBookingStatus())
                .outboxStatus(model.getOutboxStatus())
                .sagaId(model.getSagaId())
                .sagaStatus(model.getSagaStatus())
                .payload(model.getPayload())
                .createdAt(model.getCreatedAt())
                .processedAt(model.getProcessedAt())
                .version(model.getVersion())
                .build();
    }
}
