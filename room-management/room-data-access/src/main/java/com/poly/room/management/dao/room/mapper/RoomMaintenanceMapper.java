package com.poly.room.management.dao.room.mapper;

import com.poly.domain.valueobject.DateCustom;
import com.poly.domain.valueobject.MaintenanceStatus;
import com.poly.domain.valueobject.RoomId;
import com.poly.room.management.dao.room.entity.RoomMaintenanceEntity;
import com.poly.room.management.domain.entity.RoomMaintenance;
import com.poly.room.management.domain.valueobject.MaintenanceId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;


@Component
@RequiredArgsConstructor
public class RoomMaintenanceMapper {

    private final RoomMapper roomMapper;
    private final MaintenanceTypeMapper maintenanceTypeMapper;

    public RoomMaintenance toDomain(RoomMaintenanceEntity entity) {
        return RoomMaintenance.Builder.builder()
                .id(new MaintenanceId(entity.getMaintenanceId()))
                .room(roomMapper.toDomain(entity.getRoom()))
                .scheduledDate(entity.getScheduledDate() != null ? DateCustom.of(entity.getScheduledDate().toLocalDateTime()) : null)
                .status(entity.getStatus() != null ? MaintenanceStatus.valueOf(entity.getStatus()) : MaintenanceStatus.PENDING)
                .maintenanceType(maintenanceTypeMapper.toDomain(entity.getMaintenanceType()))
                .description(entity.getDescription())
                .build();
    }

    public RoomMaintenanceEntity toJpaEntity(RoomMaintenance domain) {
        return RoomMaintenanceEntity.builder()
                .maintenanceId(domain.getId().getValue())
                .room(roomMapper.toEntity(domain.getRoom()))
                .scheduledDate(domain.getScheduledDate() != null ? Timestamp.valueOf(domain.getScheduledDate().getValue()) : null)
                .status(domain.getStatus() != null ? domain.getStatus().name() : MaintenanceStatus.PENDING.name())
                .maintenanceType(maintenanceTypeMapper.toEntity(domain.getMaintenanceType()))
                .description(domain.getDescription())
                .build();
    }

    public RoomMaintenanceEntity toEntity(RoomMaintenance domain) {
        return toJpaEntity(domain);
    }
}
