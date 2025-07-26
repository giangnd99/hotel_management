package com.poly.room.management.domain.mapper;

import com.poly.domain.valueobject.RoomId;
import com.poly.room.management.domain.dto.request.CreateRoomRequest;
import com.poly.room.management.domain.dto.request.UpdateRoomRequest;
import com.poly.room.management.domain.dto.response.RoomResponse;
import com.poly.room.management.domain.entity.Room;
import com.poly.room.management.domain.entity.RoomType;
import com.poly.room.management.domain.port.out.repository.RoomTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomDtoMapper {

    private final RoomTypeRepository roomTypeRepository;
    private final RoomTypeDtoMapper roomTypeMapper;

    public Room toEntity(CreateRoomRequest request) {

        RoomType roomType = roomTypeRepository.findById(request.getRoomTypeId()).get();

        return Room.Builder.builder()
                .roomNumber(request.getRoomNumber())
                .floor(request.getFloor())
                .roomType(roomType)
                .build();
    }

    public Room toEntity(UpdateRoomRequest request) {
        RoomType roomType = roomTypeRepository.findById(request.getRoomTypeId()).get();
        return Room.Builder.builder()
                .id(new RoomId(request.getRoomId()))
                .roomNumber(request.getRoomNumber())
                .floor(request.getFloor())
                .roomType(roomType)
                .build();
    }

    public RoomResponse toResponse(Room room) {
        return RoomResponse.builder()
                .id(room.getId().getValue())
                .roomNumber(room.getRoomNumber())
                .floor(room.getFloor())
                .roomType(roomTypeMapper.toResponse(room.getRoomType()))
                .roomStatus(room.getRoomStatus().getRoomStatus())
                .build();
    }
}