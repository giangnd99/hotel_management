package com.poly.booking.management.dao.outbox.notifi.mapper;

import com.poly.booking.management.dao.outbox.notifi.entity.NotificationOutboxEntity;
import com.poly.booking.management.domain.outbox.model.NotifiOutboxMessage;
import org.springframework.stereotype.Component;

@Component
public class NotificationOutboxDataMapper {

    public NotificationOutboxEntity toEntity(NotifiOutboxMessage model) {
        return NotificationOutboxEntity.builder()
                .id(model.getId())
                .type(model.getType())
                .payload(model.getPayload())
                .sagaId(model.getSagaId())
                .sagaStatus(model.getSagaStatus())
                .outboxStatus(model.getOutboxStatus())
                .createdAt(model.getCreatedAt())
                .processedAt(model.getProcessedAt())
                .version(model.getVersion())
                .build();
    }

    public NotifiOutboxMessage toModel(NotificationOutboxEntity entity) {
        return NotifiOutboxMessage.builder()
                .id(entity.getId())
                .type(entity.getType())
                .payload(entity.getPayload())
                .sagaId(entity.getSagaId())
                .sagaStatus(entity.getSagaStatus())
                .outboxStatus(entity.getOutboxStatus())
                .createdAt(entity.getCreatedAt())
                .processedAt(entity.getProcessedAt())
                .version(entity.getVersion())
                .build();
    }
}
