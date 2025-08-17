package com.poly.room.management.domain.port.in.listener.request;


import com.poly.message.model.room.RoomRequestMessage;

public interface OrderAddRoomListener {

    void onOrderRoom(RoomRequestMessage roomRequestMessage);
}
