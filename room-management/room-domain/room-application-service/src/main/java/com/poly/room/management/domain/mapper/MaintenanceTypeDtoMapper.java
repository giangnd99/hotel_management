package com.poly.room.management.domain.mapper;

import com.poly.room.management.domain.dto.response.MaintenanceTypeResponse;
import com.poly.room.management.domain.entity.MaintenanceType;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceTypeDtoMapper {

    public MaintenanceTypeResponse toResponse(MaintenanceType maintenanceType) {
        return MaintenanceTypeResponse.builder()
                .id(maintenanceType.getId().getValue())
                .name(maintenanceType.getName())
                .build();
    }
}
