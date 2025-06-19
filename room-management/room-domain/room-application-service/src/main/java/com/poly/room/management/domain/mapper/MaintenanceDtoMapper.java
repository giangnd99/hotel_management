package com.poly.room.management.domain.mapper;

import com.poly.room.management.domain.dto.request.CreateMaintenanceRequest;
import com.poly.room.management.domain.dto.response.MaintenanceTypeResponse;
import com.poly.room.management.domain.dto.response.RoomMaintenanceResponse;
import com.poly.room.management.domain.entity.MaintenanceType;
import com.poly.room.management.domain.entity.RoomMaintenance;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceDtoMapper {

    public RoomMaintenance toEntity(CreateMaintenanceRequest request) {
        return RoomMaintenance.Builder.builder()
                .roomId(request.getRoomId())
                .staffId(request.getStaffId())
                .maintenanceDate(request.getMaintenanceDate())
                .maintenanceType(MaintenanceType.builder()
                        .id(request.getMaintenanceTypeId())
                        .build())
                .description(request.getDescription())
                .build();
    }

    public RoomMaintenanceResponse toResponse(RoomMaintenance maintenance) {
        return RoomMaintenanceResponse.builder()
                .id(maintenance.getId())
                .roomId(maintenance.getRoomId())
                .staffId(maintenance.getStaffId())
                .maintenanceDate(maintenance.getMaintenanceDate())
                .actualStartDate(maintenance.getActualStartDate())
                .completionDate(maintenance.getCompletionDate())
                .maintenanceTypeId(maintenance.getMaintenanceType().getId())
                .maintenanceTypeName(maintenance.getMaintenanceType().getName())
                .description(maintenance.getDescription())
                .maintenanceStatus(maintenance.getMaintenanceStatus().name())
                .build();
    }

    public MaintenanceTypeResponse toResponse(MaintenanceType maintenanceType) {
        return MaintenanceTypeResponse.builder()
                .id(maintenanceType.getId())
                .name(maintenanceType.getName())
                .build();
    }
}