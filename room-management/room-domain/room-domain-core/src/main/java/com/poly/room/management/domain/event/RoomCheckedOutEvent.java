package com.poly.room.management.domain.event;

import com.poly.domain.valueobject.DateCustom;
import com.poly.room.management.domain.entity.Room;

public class RoomCheckedOutEvent extends RoomEvent {
    public RoomCheckedOutEvent(Room room, DateCustom createdAt) {
        super(room, createdAt);
    }
}
