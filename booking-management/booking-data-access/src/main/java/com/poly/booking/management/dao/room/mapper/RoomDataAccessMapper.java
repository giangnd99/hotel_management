package com.poly.booking.management.dao.room.mapper;

import com.poly.booking.management.dao.room.entity.RoomEntity;
import com.poly.booking.management.domain.entity.Room;
import com.poly.domain.valueobject.RoomStatus;
import com.poly.domain.valueobject.Money;
import com.poly.domain.valueobject.RoomId;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoomDataAccessMapper {


    public List<Room> roomEntityToRooms(List<RoomEntity> roomsEntity) {
        return roomsEntity.stream().map(this::roomEntityToRoom).toList();
    }

    public RoomEntity roomToRoomEntity(Room room) {
        return RoomEntity.builder()
                .id(room.getId().getValue())
                .roomNumber(room.getRoomNumber())
                .price(room.getBasePrice().getAmount())
                .status(room.getStatus())
                .build();
    }

    public Room roomEntityToRoom(RoomEntity roomEntity) {
        return Room.Builder.builder()
                .id(new RoomId(roomEntity.getId()))
                .roomNumber(roomEntity.getRoomNumber())
                .basePrice(new Money(roomEntity.getPrice()))
                .status(roomEntity.getStatus())
                .build();
    }
}
