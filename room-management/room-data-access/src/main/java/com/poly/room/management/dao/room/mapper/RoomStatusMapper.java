package com.poly.room.management.dao.room.mapper;

import com.poly.domain.valueobject.ERoomStatus;
import com.poly.room.management.dao.room.entity.RoomStatusEntity;
import com.poly.room.management.domain.entity.RoomStatus;
import com.poly.room.management.domain.valueobject.RoomStatusId;
import org.springframework.stereotype.Component;

@Component
public class RoomStatusMapper {

    public RoomStatus toDomain(RoomStatusEntity entity) {
        return RoomStatus.Builder.builder()
                .id(new RoomStatusId(entity.getStatusId()))
                .roomStatus(ERoomStatus.valueOf(entity.getStatusName()))
                .build();
    }

    public RoomStatusEntity toEntity(RoomStatus domain) {
        return RoomStatusEntity.builder()
                .statusId(domain.getId().getValue())
                .statusName(domain.getRoomStatus())
                .build();
    }
}