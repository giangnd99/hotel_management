package com.poly.restaurant.application.port.in.message.listener;

import com.poly.message.model.room.RoomResponseMessage;

public interface RoomResponseListener {
    void onRoomMergeSuccess(RoomResponseMessage roomResponseMessage);

    void onRoomMergeFailure(RoomResponseMessage roomResponseMessage);

}
