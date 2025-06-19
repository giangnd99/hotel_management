package com.poly.room.management.dao.room.mapper;


import com.poly.domain.valueobject.ERoomStatus;
import com.poly.domain.valueobject.RoomId;
import com.poly.room.management.dao.room.entity.RoomEntity;
import com.poly.room.management.domain.entity.Room;
import com.poly.room.management.domain.entity.RoomStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomMapper {

    private final RoomTypeMapper roomTypeMapper;
    private final RoomStatusMapper roomStatusMapper;

    public Room toDomain(RoomEntity entity) {

        return Room.Builder.builder()
                .id(new RoomId(entity.getRoomId()))
                .roomType(roomTypeMapper.toDomainEntity(entity.getRoomType()))
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
