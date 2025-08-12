package com.poly.restaurant.application.port.in.message.listener;

public interface RoomResponseListener {
    void onRoomMergeSuccess(String roomId);
    void onRoomMergeFailure();

}
