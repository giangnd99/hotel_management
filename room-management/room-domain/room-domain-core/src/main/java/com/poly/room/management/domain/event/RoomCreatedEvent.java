package com.poly.room.management.domain.event;

import com.poly.domain.valueobject.DateCustom;
import com.poly.room.management.domain.entity.Room;

public class RoomCreatedEvent extends RoomEvent {
    public RoomCreatedEvent(Room room, DateCustom createdAt) {
        super(room, createdAt);
    }
}
