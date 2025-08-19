package com.poly.room.management.domain.service;

import com.poly.room.management.domain.entity.Room;
import com.poly.room.management.domain.event.RoomCancellationEvent;

import java.util.List;

public interface RoomCancellationService {

    public List<Room> getCancellableRooms(List<Room> rooms);

    public boolean canCancelRoom(Room room);

    public RoomCancellationEvent cancelRoom(Room room, String bookingId, String cancellationReason);
}
