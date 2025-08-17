package com.poly.room.management.domain.service.impl;

import com.poly.domain.valueobject.DateCustom;
import com.poly.room.management.domain.entity.Room;
import com.poly.room.management.domain.event.*;
import com.poly.room.management.domain.service.RoomEventService;

public class RoomEventServiceImpl implements RoomEventService {

    @Override
    public RoomCreatedEvent createdRoom(Room room) {
        room.validate();
        room.setVacantRoomStatus();
        return new RoomCreatedEvent(room, DateCustom.now());
    }

    @Override
    public RoomBookedEvent bookedRoom(Room room) {
        room.setBookedRoomStatus();
        return new RoomBookedEvent(room, DateCustom.now());
    }

    @Override
    public RoomCheckedInEvent checkedInRoom(Room room) {
        room.setOccupiedRoomStatus();
        return new RoomCheckedInEvent(room, DateCustom.now());
    }

    @Override
    public RoomVacatedEvent vacatedRoom(Room room) {
        room.setVacantRoomStatus();
        return new RoomVacatedEvent(room, DateCustom.now());
    }

    @Override
    public RoomMaintainedEvent maintainedRoom(Room room) {
        room.setMaintenanceRoomStatus();
        return new RoomMaintainedEvent(room, DateCustom.now());
    }

    @Override
    public RoomCleanedEvent cleanedRoom(Room room) {
        room.setCleanRoomStatus();
        return new RoomCleanedEvent(room, DateCustom.now());
    }

    @Override
    public RoomCheckedOutEvent checkedOutRoom(Room room) {
        room.setCheckOutRoomStatus();
        return new RoomCheckedOutEvent(room, DateCustom.now());
    }
}
