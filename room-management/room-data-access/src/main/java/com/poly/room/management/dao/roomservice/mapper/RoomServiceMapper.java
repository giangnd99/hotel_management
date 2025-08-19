package com.poly.room.management.dao.roomservice.mapper;

import com.poly.room.management.dao.roomservice.entity.RoomServiceEntity;
import com.poly.room.management.domain.entity.RoomService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class RoomServiceMapper {

    public RoomService toDomain(RoomServiceEntity entity) {
        if (entity == null) {
            return null;
        }

        return RoomService.builder()
                .serviceId(entity.getServiceId())
                .roomNumber(entity.getRoomNumber())
                .guestId(entity.getGuestId())
                .guestName(entity.getGuestName())
                .serviceType(entity.getServiceType())
                .serviceName(entity.getServiceName())
                .description(entity.getDescription())
                .quantity(entity.getQuantity())
                .unitPrice(entity.getUnitPrice())
                .totalPrice(entity.getTotalPrice())
                .status(entity.getStatus() != null ? entity.getStatus().name() : null)
                .requestedAt(entity.getRequestedAt())
                .completedAt(entity.getCompletedAt())
                .requestedBy(entity.getRequestedBy())
                .completedBy(entity.getCompletedBy())
                .notes(entity.getNotes())
                .specialInstructions(entity.getSpecialInstructions())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public RoomServiceEntity toEntity(RoomService domain) {
        if (domain == null) {
            return null;
        }

        RoomServiceEntity.ServiceStatus status = null;
        if (domain.getStatus() != null) {
            try {
                status = RoomServiceEntity.ServiceStatus.valueOf(domain.getStatus());
            } catch (IllegalArgumentException e) {
                // Handle unknown status values
                status = RoomServiceEntity.ServiceStatus.REQUESTED;
            }
        }

        return RoomServiceEntity.builder()
                .serviceId(domain.getServiceId())
                .roomNumber(domain.getRoomNumber())
                .guestId(domain.getGuestId())
                .guestName(domain.getGuestName())
                .serviceType(domain.getServiceType())
                .serviceName(domain.getServiceName())
                .description(domain.getDescription())
                .quantity(domain.getQuantity())
                .unitPrice(domain.getUnitPrice())
                .totalPrice(domain.getTotalPrice())
                .status(status)
                .requestedAt(domain.getRequestedAt())
                .completedAt(domain.getCompletedAt())
                .requestedBy(domain.getRequestedBy())
                .completedBy(domain.getCompletedBy())
                .notes(domain.getNotes())
                .specialInstructions(domain.getSpecialInstructions())
                .createdAt(domain.getCreatedAt() != null ? domain.getCreatedAt() : LocalDateTime.now())
                .updatedAt(domain.getUpdatedAt() != null ? domain.getUpdatedAt() : LocalDateTime.now())
                .build();
    }
}
