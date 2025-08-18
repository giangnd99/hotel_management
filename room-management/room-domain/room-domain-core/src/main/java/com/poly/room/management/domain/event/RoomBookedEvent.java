package com.poly.room.management.domain.event;

import com.poly.domain.valueobject.DateCustom;
import com.poly.room.management.domain.entity.Room;

public class RoomBookedEvent extends RoomEvent {
    public RoomBookedEvent(Room room, DateCustom createdAt) {
        super(room, createdAt);
    }
}
