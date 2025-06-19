package com.poly.room.management.dao.room.mapper;

import com.poly.domain.valueobject.MaintenanceStatus;
import com.poly.domain.valueobject.RoomId;
import com.poly.room.management.dao.room.entity.RoomMaintenanceEntity;
import com.poly.room.management.domain.entity.RoomMaintenance;
import com.poly.room.management.domain.valueobject.MaintenanceId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class RoomMaintenanceMapper {

    private final RoomMapper roomMapper;
    private final MaintenanceTypeMapper maintenanceTypeMapper;

    public RoomMaintenance toDomain(RoomMaintenanceEntity entity) {
        return RoomMaintenance.Builder.builder()
                .id(new MaintenanceId(entity.getMaintenanceId()))
                .room(roomMapper.toDomain(entity.getRoom()))
                .maintenanceDate(entity.getMaintenanceDate())
                .maintenanceStatus(MaintenanceStatus.valueOf(entity.getStatus()))
                .maintenanceType(maintenanceTypeMapper.toDomain(entity.getMaintenanceType()))
                .build();
    }

    public RoomMaintenanceEntity toJpaEntity(RoomMaintenance domain) {
        return RoomMaintenanceEntity.builder()
                .maintenanceId(domain.getId().getValue())
                .room(roomMapper.toEntity(domain.getRoom()))
                .maintenanceDate(domain.getMaintenanceDate())
                .status(domain.getMaintenanceStatus().name())
                .maintenanceType(maintenanceTypeMapper.toEntity(domain.getMaintenanceType()))
                .build();
    }
}
