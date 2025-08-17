package com.poly.room.management.kafka.adaptor;

import com.poly.room.management.domain.command.RoomBookingCommand;
import com.poly.room.management.domain.port.in.listener.request.BookingRoomReserveListener;
import com.poly.room.management.domain.message.BookingRoomRequestMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingRoomReserveListenerImpl implements BookingRoomReserveListener {

    private final RoomBookingCommand roomBookingCommand;

    @Override
    public void onBookingRoomReserve(BookingRoomRequestMessage roomRequestMessage) {
        roomBookingCommand.process(roomRequestMessage);
    }
}
