package com.poly.room.management.domain.event;

import com.poly.domain.valueobject.DateCustom;
import com.poly.room.management.domain.entity.Room;

public class RoomMaintainedEvent extends RoomEvent {
    public RoomMaintainedEvent(Room room, DateCustom createdAt) {
        super(room, createdAt);
    }
}
