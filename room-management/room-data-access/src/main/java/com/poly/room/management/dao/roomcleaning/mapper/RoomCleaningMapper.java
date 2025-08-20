package com.poly.room.management.dao.roomcleaning.mapper;

import com.poly.room.management.dao.roomcleaning.entity.RoomCleaningEntity;
import com.poly.room.management.domain.entity.RoomCleaning;
import org.springframework.stereotype.Component;

@Component
public class RoomCleaningMapper {

    public RoomCleaning toDomain(RoomCleaningEntity entity) {
        if (entity == null) {
            return null;
        }

        return RoomCleaning.builder()
                .id(entity.getId())
                .roomId(entity.getRoomId())
                .roomNumber(entity.getRoomNumber())
                .cleaningType(entity.getCleaningType() != null ? entity.getCleaningType().name() : null)
                .priority(entity.getPriority() != null ? entity.getPriority().name() : null)
                .status(entity.getStatus() != null ? entity.getStatus().name() : null)
                .description(entity.getDescription())
                .notes(entity.getNotes())
                .requestedBy(entity.getRequestedBy())
                .assignedTo(entity.getAssignedTo())
                .requestedAt(entity.getRequestedAt())
                .scheduledAt(entity.getScheduledAt())
                .startedAt(entity.getStartedAt())
                .completedAt(entity.getCompletedAt())
                .isUrgent(entity.getIsUrgent())
                .specialInstructions(entity.getSpecialInstructions())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public RoomCleaningEntity toEntity(RoomCleaning domain) {
        if (domain == null) {
            return null;
        }

        return RoomCleaningEntity.builder()
                .id(domain.getId())
                .roomId(domain.getRoomId())
                .roomNumber(domain.getRoomNumber())
                .cleaningType(domain.getCleaningType() != null ? 
                    RoomCleaningEntity.CleaningType.valueOf(domain.getCleaningType()) : null)
                .priority(domain.getPriority() != null ? 
                    RoomCleaningEntity.CleaningPriority.valueOf(domain.getPriority()) : null)
                .status(domain.getStatus() != null ? 
                    RoomCleaningEntity.CleaningStatus.valueOf(domain.getStatus()) : null)
                .description(domain.getDescription())
                .notes(domain.getNotes())
                .requestedBy(domain.getRequestedBy())
                .assignedTo(domain.getAssignedTo())
                .requestedAt(domain.getRequestedAt())
                .scheduledAt(domain.getScheduledAt())
                .startedAt(domain.getStartedAt())
                .completedAt(domain.getCompletedAt())
                .isUrgent(domain.getIsUrgent())
                .specialInstructions(domain.getSpecialInstructions())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}
