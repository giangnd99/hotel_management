package com.poly.room.management.domain.service;

import com.poly.room.management.domain.entity.Room;
import com.poly.room.management.domain.event.*;

public interface RoomEventService {

    RoomCreatedEvent createdRoom(Room room);

    RoomBookedEvent bookedRoom(Room room);

    RoomCheckedInEvent checkedInRoom(Room room);

    RoomVacatedEvent vacatedRoom(Room room);

    RoomMaintainedEvent maintainedRoom(Room room);

    RoomCleanedEvent cleanedRoom(Room room);

    RoomCheckedOutEvent checkedOutRoom(Room room);
}
