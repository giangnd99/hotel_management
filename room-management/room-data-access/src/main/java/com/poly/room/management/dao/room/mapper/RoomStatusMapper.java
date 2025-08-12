package com.poly.room.management.dao.room.mapper;

import com.poly.domain.valueobject.RoomStatus;
import com.poly.room.management.dao.room.entity.RoomStatusEntity;
import org.springframework.stereotype.Component;

@Component
public class RoomStatusMapper {

    public RoomStatus toDomain(RoomStatusEntity entity) {
        return RoomStatus.Builder.builder()
                .id(new RoomStatusId(entity.getStatusId()))
                .roomStatus(RoomStatus.valueOf(entity.getStatusName()))
                .build();
    }

    public RoomStatusEntity toEntity(RoomStatus domain) {
        return RoomStatusEntity.builder()
                .statusId(domain.getId().getValue())
                .statusName(domain.getRoomStatus())
                .build();
    }
}