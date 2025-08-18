package com.poly.room.management.domain.event;

import com.poly.domain.valueobject.DateCustom;
import com.poly.room.management.domain.entity.Room;

public class RoomCheckedInEvent extends RoomEvent {
    public RoomCheckedInEvent(Room room, DateCustom createdAt) {
        super(room, createdAt);
    }
}
