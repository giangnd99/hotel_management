package com.poly.booking.management.domain.saga.impl;

import org.springframework.stereotype.Service;

import com.poly.booking.management.domain.dto.message.RoomMessageResponse;
import com.poly.booking.management.domain.port.in.message.listener.room.RoomReservedListener;
import com.poly.booking.management.domain.saga.room.BookingRoomSaga;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomReservedListenerImpl implements RoomReservedListener {

    private final BookingRoomSaga bookingRoomSaga;

    @Override
    public void roomReserved(RoomMessageResponse roomReservedResponseMessage) {
        bookingRoomSaga.process(roomReservedResponseMessage);
        log.info("Room reserved for booking: {}", roomReservedResponseMessage.getBookingId());

    }

    @Override
    public void roomReservationCancelled(RoomMessageResponse roomReservedResponseMessage) {
        bookingRoomSaga.rollback(roomReservedResponseMessage);
        log.info("Room reservation cancelled for booking: {}", roomReservedResponseMessage.getBookingId());
    }
}
