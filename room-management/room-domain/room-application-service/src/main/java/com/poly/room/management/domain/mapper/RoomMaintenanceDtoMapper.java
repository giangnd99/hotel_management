package com.poly.room.management.domain.mapper;

import com.poly.room.management.domain.dto.response.RoomMaintenanceResponse;
import com.poly.room.management.domain.entity.RoomMaintenance;
import org.springframework.stereotype.Component;

@Component
public class RoomMaintenanceDtoMapper {

    public RoomMaintenanceResponse toResponse(RoomMaintenance roomMaintenance) {
        return RoomMaintenanceResponse.builder()
                .id(roomMaintenance.getId().getValue().hashCode()) // Convert UUID to Integer using hashCode
                .roomId(roomMaintenance.getRoom().getId().getValue().toString())
                .staffId(roomMaintenance.getStaffId() != null ? roomMaintenance.getStaffId().getValue().toString() : null)
                .scheduledDate(roomMaintenance.getScheduledDate() != null ? 
                    java.sql.Timestamp.valueOf(roomMaintenance.getScheduledDate().getValue()) : null)
                .startDate(roomMaintenance.getStartDate() != null ? 
                    java.sql.Timestamp.valueOf(roomMaintenance.getStartDate().getValue()) : null)
                .completionDate(roomMaintenance.getCompletionDate() != null ? 
                    java.sql.Timestamp.valueOf(roomMaintenance.getCompletionDate().getValue()) : null)
                .maintenanceTypeId(roomMaintenance.getMaintenanceType().getId().getValue())
                .maintenanceTypeName(roomMaintenance.getMaintenanceType().getName())
                .description(roomMaintenance.getDescription())
                .maintenanceStatus(roomMaintenance.getStatus().name())
                .build();
    }
}
