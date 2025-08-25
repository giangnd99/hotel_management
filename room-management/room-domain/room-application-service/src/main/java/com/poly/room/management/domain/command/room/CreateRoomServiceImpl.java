package com.poly.room.management.domain.command.room;

import com.poly.domain.valueobject.RoomId;
import com.poly.domain.valueobject.RoomStatus;
import com.poly.room.management.domain.dto.request.CreateRoomRequest;
import com.poly.room.management.domain.entity.Room;
import com.poly.room.management.domain.entity.RoomType;
import com.poly.room.management.domain.port.out.repository.RoomRepository;
import com.poly.room.management.domain.port.out.repository.RoomTypeRepository;
import com.poly.room.management.domain.service.CreateRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateRoomServiceImpl implements CreateRoomService {

    private final RoomRepository repository;
    private final RoomTypeRepository roomTypeRepository;

    @Override
    @Transactional
    public Room processRoomCreation(CreateRoomRequest request) {
        Room room = validateAndMappingRoomRequest(request);
        return repository.save(room);
    }


    private Room validateAndMappingRoomRequest(CreateRoomRequest request) {
        assert request != null;
        assert request.getRoomTypeId() != null;
        String roomNumber = request.getRoomNumber();
        assert request.getFloor() != null;
        Integer floor = request.getFloor();
        assert null != roomNumber && !roomNumber.isBlank();
        UUID roomTypeId = request.getRoomTypeId();
        RoomType roomType = findRoomType(roomTypeId);
        RoomStatus status = RoomStatus.VACANT;
        String area = request.getArea() != null ? request.getArea() : "40m2" ;

        RoomId roomId = new RoomId(UUID.randomUUID());

        return Room.Builder.builder()
                .roomNumber(roomNumber)
                .floor(floor)
                .roomType(roomType)
                .roomStatus(status)
                .area(area)
                .id(roomId)
                .build();
    }

    private RoomType findRoomType(UUID roomTypeId) {
        return roomTypeRepository.findById(roomTypeId).orElseThrow(() -> new RuntimeException("Room type not found"));
    }
}
