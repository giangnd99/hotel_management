package com.poly.room.management.domain.event;

import com.poly.domain.event.DomainEvent;
import com.poly.domain.valueobject.DateCustom;
import com.poly.room.management.domain.entity.Room;

public abstract class RoomEvent implements DomainEvent<Room> {

    private final Room room;
    private final DateCustom createdAt;

    public RoomEvent(Room room, DateCustom createdAt) {
        this.room = room;
        this.createdAt = createdAt;
    }

    public Room getRoom() {
        return room;
    }

    public DateCustom getCreatedAt() {
        return createdAt;
    }
}
