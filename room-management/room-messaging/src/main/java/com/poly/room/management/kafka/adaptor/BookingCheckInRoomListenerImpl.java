package com.poly.room.management.kafka.adaptor;

import com.poly.room.management.domain.command.RoomBookingCommand;
import com.poly.room.management.domain.command.RoomCheckInCommand;
import com.poly.room.management.domain.message.BookingRoomRequestMessage;
import com.poly.room.management.domain.port.in.listener.request.BookingCheckInRoomListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingCheckInRoomListenerImpl implements BookingCheckInRoomListener {


    private final RoomCheckInCommand roomCheckInCommand;
    @Override
    public void onCheckInRoom(BookingRoomRequestMessage roomRequestMessage) {

        roomCheckInCommand.process(roomRequestMessage);
    }
}
