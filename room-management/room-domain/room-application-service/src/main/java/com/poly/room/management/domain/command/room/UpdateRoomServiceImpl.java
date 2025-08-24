package com.poly.room.management.domain.command.room;

import com.poly.domain.valueobject.Money;
import com.poly.room.management.domain.dto.request.UpdateRoomRequest;
import com.poly.room.management.domain.entity.Room;
import com.poly.room.management.domain.entity.RoomType;
import com.poly.room.management.domain.port.out.repository.RoomRepository;
import com.poly.room.management.domain.port.out.repository.RoomTypeRepository;
import com.poly.room.management.domain.service.UpdateRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateRoomServiceImpl implements UpdateRoomService {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;

    @Override
    public Room updatedRoom(UUID roomId, UpdateRoomRequest room) {
        try {


        if (roomId == null) {
            throw new RuntimeException("Room id cannot be empty");
        }
        Room foundedRoom = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));

        if (room.getFloor() != null) {
            foundedRoom.setFloor(room.getFloor());
        }
        if (room.getRoomTypeId() != null) {
            RoomType roomType = findRoomType(room.getRoomTypeId());
            foundedRoom.setRoomType(roomType);
        }
        if (room.getArea() != null){
            foundedRoom.setArea(room.getArea());
        }
        if (room.getBasePrice() != null){
            foundedRoom.setRoomPrice(new Money(room.getBasePrice()));
        }

        return roomRepository.save(foundedRoom);
    }catch (Exception e){

            log.error("Failed to update room for id: {}. Reason: {}", roomId, e.getMessage());
            throw new RuntimeException("Failed to update room for id: " + roomId + ". Reason: " + e.getMessage());
        }
    }


    private RoomType findRoomType(UUID roomTypeId) {
        return roomTypeRepository.findById(roomTypeId).orElseThrow(() -> new RuntimeException("Room type not found"));
    }
}

