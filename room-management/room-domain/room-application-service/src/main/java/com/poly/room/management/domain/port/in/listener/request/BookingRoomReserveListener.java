package com.poly.room.management.domain.port.in.listener.request;

import com.poly.room.management.domain.message.BookingRoomRequestMessage;

public interface BookingRoomReserveListener {
    // giữ phòng sau khi cọc -> đến ngày check-in
    void onBookingRoomReserve(BookingRoomRequestMessage roomRequestMessage);
//
//    //Hoan tien
//    void onCancelBookingRoomReserve(Integer bookingId);

}
