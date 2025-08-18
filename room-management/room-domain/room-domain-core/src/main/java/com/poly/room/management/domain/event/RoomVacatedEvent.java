package com.poly.room.management.domain.event;

import com.poly.domain.valueobject.DateCustom;
import com.poly.room.management.domain.entity.Room;

public class RoomVacatedEvent extends RoomEvent {
    public RoomVacatedEvent(Room room, DateCustom createdAt) {
        super(room, createdAt);
    }
}
