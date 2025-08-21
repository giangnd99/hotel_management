package com.poly.room.management.dao.room.mapper;


import com.poly.domain.valueobject.RoomStatus;
import com.poly.domain.valueobject.RoomId;
import com.poly.room.management.dao.room.entity.RoomEntity;
import com.poly.room.management.dao.room.entity.RoomTypeFurnitureEntity;
import com.poly.room.management.dao.room.repository.RoomTypeFurnitureJpaRepository;
import com.poly.room.management.domain.entity.Room;
import com.poly.room.management.domain.entity.RoomType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoomMapper {

    private final RoomTypeMapper roomTypeMapper;
    private final RoomTypeFurnitureJpaRepository repository;

    public Room toDomain(RoomEntity entity) {

        RoomType roomType = roomTypeMapper.toDomainEntity(entity.getRoomType());
        return Room.Builder.builder()
                .id(new RoomId(entity.getRoomId()))
                .roomType(roomType)
                .roomStatus(entity.getRoomStatus())
                .roomNumber(entity.getRoomNumber())
                .floor(entity.getFloor())
                .roomNumber(entity.getImage_url())
                .build();
    }

    public RoomEntity toEntity(Room domain) {
        return RoomEntity.builder()
                .roomId(domain.getId().getValue())
                .roomType(roomTypeMapper.toEntity(domain.getRoomType()))
                .roomStatus(domain.getRoomStatus())
                .roomNumber(domain.getRoomNumber())
                .image_url(domain.getImage_url())
                .floor(domain.getFloor())
                .build();
    }
}
