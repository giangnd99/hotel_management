package com.poly.room.management.dao.room.mapper;


import com.poly.domain.valueobject.ERoomStatus;
import com.poly.domain.valueobject.Money;
import com.poly.domain.valueobject.RoomId;
import com.poly.room.management.dao.room.entity.RoomEntity;
import com.poly.room.management.dao.room.entity.RoomTypeFurnitureEntity;
import com.poly.room.management.dao.room.repository.RoomTypeFurnitureJpaRepository;
import com.poly.room.management.domain.entity.Room;
import com.poly.room.management.domain.entity.RoomStatus;
import com.poly.room.management.domain.entity.RoomType;
import com.poly.room.management.domain.valueobject.RoomTypeId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoomMapper {

    private final RoomTypeMapper roomTypeMapper;
    private final RoomStatusMapper roomStatusMapper;
    private final RoomTypeFurnitureMapper roomTypeFurnitureMapper;
    private final RoomTypeFurnitureJpaRepository repository;

    public Room toDomain(RoomEntity entity) {
        List<RoomTypeFurnitureEntity> furnitureEntities = repository.findAllByRoomType_RoomTypeId(
                entity.getRoomType().getRoomTypeId());
        RoomType roomType = roomTypeMapper.toDomainEntity(entity.getRoomType());
        return Room.Builder.builder()
                .id(new RoomId(entity.getRoomId()))
                .roomType(roomType)
                .roomStatus(new RoomStatus(ERoomStatus.valueOf(entity.getStatus().getStatusName())))
                .roomNumber(entity.getRoomNumber())
                .floor(entity.getFloor())
                .build();
    }

    public RoomEntity toEntity(Room domain) {
        return RoomEntity.builder()
                .roomId(domain.getId().getValue())
                .roomType(roomTypeMapper.toEntity(domain.getRoomType()))
                .status(roomStatusMapper.toEntity(domain.getRoomStatus()))
                .roomNumber(domain.getRoomNumber())
                .floor(domain.getFloor())
                .build();
    }
}
