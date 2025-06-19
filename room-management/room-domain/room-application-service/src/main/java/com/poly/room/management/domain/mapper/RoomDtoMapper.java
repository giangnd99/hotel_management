package com.poly.room.management.domain.mapper;

import com.poly.room.management.domain.dto.request.CreateRoomRequest;
import com.poly.room.management.domain.dto.request.UpdateRoomRequest;
import com.poly.room.management.domain.dto.response.RoomResponse;
import com.poly.room.management.domain.entity.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomDtoMapper {
    private final RoomTypeDtoMapper roomTypeMapper;

    public RoomDtoMapper(RoomTypeDtoMapper roomTypeMapper) {
        this.roomTypeMapper = roomTypeMapper;
    }

    public Room toEntity(CreateRoomRequest request) {
        return Room.Builder.builder()
                .roomNumber(request.getRoomNumber())
                .floor(request.getFloor())
                .roomType(roomTypeMapper.toEntity(request.getRoomTypeId()))
                .build();
    }

    public Room toEntity(UpdateRoomRequest request) {
        return Room.Builder.builder()
                .id(request.getRoomId())
                .roomNumber(request.getRoomNumber())
                .floor(request.getFloor())
                .roomType(roomTypeMapper.toEntity(request.getRoomTypeId()))
                .build();
    }

    public RoomResponse toResponse(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .roomNumber(room.getRoomNumber())
                .floor(room.getFloor())
                .roomType(roomTypeMapper.toResponse(room.getRoomType()))
                .roomStatus(room.getRoomStatus().toString())
                .build();
    }
}