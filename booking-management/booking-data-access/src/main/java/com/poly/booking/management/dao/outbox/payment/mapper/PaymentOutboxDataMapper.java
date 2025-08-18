package com.poly.booking.management.dao.outbox.payment.mapper;

import com.poly.booking.management.dao.outbox.payment.entity.PaymentOutboxEntity;
import com.poly.booking.management.domain.outbox.model.PaymentOutboxMessage;
import org.springframework.stereotype.Component;

@Component
public class PaymentOutboxDataMapper {

    public PaymentOutboxMessage toModel(PaymentOutboxEntity entity) {
        return PaymentOutboxMessage.builder()
                .id(entity.getId())
                .type(entity.getType())
                .sagaId(entity.getSagaId())
                .sagaStatus(entity.getSagaStatus())
                .outboxStatus(entity.getOutboxStatus())
                .payload(entity.getPayload())
                .createdAt(entity.getCreatedAt())
                .processedAt(entity.getProcessedAt())
                .version(entity.getVersion())
                .build();
    }
    public PaymentOutboxEntity toEntity(PaymentOutboxMessage model){
        return PaymentOutboxEntity.builder()
                .id(model.getId())
                .type(model.getType())
                .sagaId(model.getSagaId())
                .sagaStatus(model.getSagaStatus())
                .outboxStatus(model.getOutboxStatus())
                .payload(model.getPayload())
                .createdAt(model.getCreatedAt())
                .processedAt(model.getProcessedAt())
                .version(model.getVersion())
                .build();
    }
}
