package com.poly.room.management.dao.roommaintenance.mapper;

import com.poly.room.management.dao.room.entity.RoomEntity;
import com.poly.room.management.dao.roommaintenance.entity.RoomMaintenanceEntity;
import com.poly.room.management.domain.entity.RoomMaintenance;
import org.springframework.stereotype.Component;

@Component
public class RoomMaintenanceMapper {

    public RoomMaintenance toDomain(RoomMaintenanceEntity entity) {
        if (entity == null) {
            return null;
        }

        return RoomMaintenance.builder()
                .id(entity.getId())
                .roomId(entity.getRoom().getRoomId())
                .roomNumber(entity.getRoomNumber())
                .issueType(entity.getIssueType() != null ? entity.getIssueType().name() : null)
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
                .estimatedCost(entity.getEstimatedCost())
                .actualCost(entity.getActualCost())
                .isUrgent(entity.getIsUrgent())
                .specialInstructions(entity.getSpecialInstructions())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public RoomMaintenanceEntity toEntity(RoomMaintenance domain) {
        if (domain == null) {
            return null;
        }

        return RoomMaintenanceEntity.builder()
                .id(domain.getId())
                .room(RoomEntity.builder()
                        .roomId(domain.getRoomId())
                        .build())
                .roomNumber(domain.getRoomNumber())
                .issueType(domain.getIssueType() != null ?
                        RoomMaintenanceEntity.IssueType.valueOf(domain.getIssueType()) : null)
                .priority(domain.getPriority() != null ?
                        RoomMaintenanceEntity.MaintenancePriority.valueOf(domain.getPriority()) : null)
                .status(domain.getStatus() != null ?
                        RoomMaintenanceEntity.MaintenanceStatus.valueOf(domain.getStatus()) : null)
                .description(domain.getDescription())
                .notes(domain.getNotes())
                .requestedBy(domain.getRequestedBy())
                .assignedTo(domain.getAssignedTo())
                .requestedAt(domain.getRequestedAt())
                .scheduledAt(domain.getScheduledAt())
                .startedAt(domain.getStartedAt())
                .completedAt(domain.getCompletedAt())
                .estimatedCost(domain.getEstimatedCost())
                .actualCost(domain.getActualCost())
                .isUrgent(domain.getIsUrgent())
                .specialInstructions(domain.getSpecialInstructions())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}
