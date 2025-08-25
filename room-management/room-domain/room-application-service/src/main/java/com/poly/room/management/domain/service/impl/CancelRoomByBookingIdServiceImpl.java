package com.poly.room.management.domain.service.impl;

import com.poly.domain.valueobject.RoomStatus;
import com.poly.room.management.domain.port.out.feign.BookingClient;
import com.poly.room.management.domain.port.out.repository.RoomRepository;
import com.poly.room.management.domain.service.CancelRoomByBookingIdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class CancelRoomByBookingIdServiceImpl implements CancelRoomByBookingIdService {

    private final RoomRepository roomRepository;
    @Override
    public List<UUID> cancelRoomByBookingId(List<UUID> uuidRoomList) {

        for (UUID roomId : uuidRoomList) {
            roomRepository.findById(roomId).ifPresent(room -> {
                log.info("Canceling room : {}", room.getRoomNumber());
                room.setRoomStatus(RoomStatus.VACANT);
                roomRepository.update(room);
                log.info("Successfully updated room status to VACANT for room because Cancel Booking: {}", room.getRoomNumber());
            });
        }

        return uuidRoomList;
    }
}
