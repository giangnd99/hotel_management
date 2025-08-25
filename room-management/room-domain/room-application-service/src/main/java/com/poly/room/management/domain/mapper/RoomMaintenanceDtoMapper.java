package com.poly.room.management.domain.mapper;

import com.poly.room.management.domain.dto.response.RoomMaintenanceResponse;
import com.poly.room.management.domain.entity.RoomMaintenance;
import org.springframework.stereotype.Component;

@Component
public class RoomMaintenanceDtoMapper {

    public RoomMaintenanceResponse toResponse(RoomMaintenance roomMaintenance) {
        return RoomMaintenanceResponse.builder()
                .id(roomMaintenance.getId().toString()) // Convert UUID to Integer using hashCode
                .roomId(roomMaintenance.getRoomId().toString())
                .staffId(roomMaintenance.getAssignedTo())
                .scheduledDate(roomMaintenance.getScheduledAt() != null ?
                    java.sql.Timestamp.valueOf(roomMaintenance.getScheduledAt()) : null)
                .startDate(roomMaintenance.getStartedAt() != null ?
                    java.sql.Timestamp.valueOf(roomMaintenance.getStartedAt()) : null)
                .completionDate(roomMaintenance.getCompletedAt() != null ?
                    java.sql.Timestamp.valueOf(roomMaintenance.getCompletedAt()) : null)
                .description(roomMaintenance.getDescription())
                .maintenanceStatus(roomMaintenance.getStatus())
                .build();
    }
}
