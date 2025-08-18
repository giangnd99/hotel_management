package com.poly.booking.management.messaging.adapter.listener;

import com.poly.booking.management.domain.port.in.message.listener.RoomReservedListener;
import com.poly.booking.management.domain.message.RoomMessageResponse;
import org.springframework.stereotype.Service;

import com.poly.booking.management.domain.saga.room.ReserveRoomStep;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomReservedListenerImpl implements RoomReservedListener {

    private final ReserveRoomStep reserveRoomStep;

    @Override
    public void roomReserved(RoomMessageResponse roomReservedResponseMessage) {
        reserveRoomStep.process(roomReservedResponseMessage);
        log.info("Room reserved for booking: {}", roomReservedResponseMessage.getBookingId());

    }

    @Override
    public void roomReservationCancelled(RoomMessageResponse roomReservedResponseMessage) {
        reserveRoomStep.rollback(roomReservedResponseMessage);
        log.info("Room reservation cancelled for booking: {}", roomReservedResponseMessage.getBookingId());
    }
}
