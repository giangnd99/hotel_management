package com.poly.room.management.domain.mapper;

import com.poly.domain.valueobject.DateCustom;
import com.poly.domain.valueobject.RoomId;
import com.poly.domain.valueobject.StaffId;
import com.poly.room.management.domain.dto.request.CreateMaintenanceRequest;
import com.poly.room.management.domain.dto.response.MaintenanceTypeResponse;
import com.poly.room.management.domain.dto.response.RoomMaintenanceResponse;
import com.poly.room.management.domain.entity.MaintenanceType;
import com.poly.room.management.domain.entity.Room;
import com.poly.room.management.domain.entity.RoomMaintenance;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.port.out.repository.RoomRepository;
import com.poly.room.management.domain.valueobject.MaintenanceTypeId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MaintenanceDtoMapper {

    private final RoomRepository roomRepository;

    public RoomMaintenance toEntity(CreateMaintenanceRequest request) {

        Optional<Room> room = Optional.ofNullable(roomRepository.findById(UUID.fromString(request.getRoomId()))
                .orElseThrow(() -> new RoomDomainException("Room not found")));

        if (room.isEmpty()) {
            throw new RoomDomainException("Room not found");
        }
        Room roomEntity = room.get();
        return RoomMaintenance.Builder.builder()
                .room(roomEntity)
                .staffId(new StaffId(request.getStaffId()))
                .scheduledDate(DateCustom.of(request.getMaintenanceDate()))
                .maintenanceType(
                        MaintenanceType.Builder.builder()
                                .id(new MaintenanceTypeId(request.getMaintenanceTypeId()))
                                .name(request.getMaintenanceName())
                                .build())
                .description(request.getDescription())
                .build();
    }

    public RoomMaintenanceResponse toResponse(RoomMaintenance maintenance) {
        return RoomMaintenanceResponse.builder()
                .id(maintenance.getId().getValue())
                .roomId(maintenance.getRoom().getId().getValue().toString())
                .staffId(maintenance.getStaffId().getValue())
                .scheduledDate(Timestamp.valueOf(maintenance.getScheduledDate().getValue()))
                .startDate(Timestamp.valueOf(maintenance.getStartDate().getValue()))
                .completionDate(Timestamp.valueOf(maintenance.getCompletionDate().getValue()))
                .maintenanceTypeId(maintenance.getMaintenanceType().getId().getValue())
                .maintenanceTypeName(maintenance.getMaintenanceType().getName())
                .description(maintenance.getDescription())
                .maintenanceStatus(maintenance.getStatus().name())
                .build();
    }

    public MaintenanceTypeResponse toResponse(MaintenanceType maintenanceType) {
        return MaintenanceTypeResponse.builder()
                .id(maintenanceType.getId().getValue())
                .name(maintenanceType.getName())
                .build();
    }
}