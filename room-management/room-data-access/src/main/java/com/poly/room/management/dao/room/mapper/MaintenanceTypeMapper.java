package com.poly.room.management.dao.room.mapper;

import com.poly.room.management.dao.room.entity.MaintenanceTypeEntity;
import com.poly.room.management.domain.entity.MaintenanceType;
import com.poly.room.management.domain.valueobject.MaintenanceTypeId;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceTypeMapper {

    public MaintenanceType toDomain(MaintenanceTypeEntity entity) {
        return MaintenanceType.Builder.builder()
                .id(new MaintenanceTypeId(entity.getMaintenanceTypeId()))
                .name(entity.getName())
                .build();
    }

    public MaintenanceTypeEntity toEntity(MaintenanceType domain) {
        return MaintenanceTypeEntity.builder()
                .maintenanceTypeId(domain.getId().getValue())
                .name(domain.getName())
                .build();
    }
}
