package com.poly.room.management.domain.service.impl;

import com.poly.domain.valueobject.RoomStatus;
import com.poly.domain.valueobject.InventoryItemId;
import com.poly.domain.valueobject.StaffId;
import com.poly.room.management.domain.entity.Room;
import com.poly.room.management.domain.entity.RoomType;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.service.sub.RoomCommandService;

public class RoomCommandServiceImpl implements RoomCommandService {
    @Override
    public Room createRoom(String roomNumber, int floor, RoomType roomType) {
        Room newRoom = Room.Builder.builder()
                .roomNumber(roomNumber)
                .floor(floor)
                .roomType(roomType)
                .build();
        newRoom.setRoomStatus(RoomStatus.VACANT);
        newRoom.validate();
        return newRoom;
    }

    @Override
    public Room updateRoomDetails(Room room, String newRoomNumber, int newFloor, RoomType newRoomType) {
        room.setRoomNumber(newRoomNumber);
        room.setFloor(newFloor);
        room.setRoomType(newRoomType);
        room.validate();
        return room;
    }

    @Override
    public void deleteRoom(Room room) {
        if (room.getRoomStatus() == RoomStatus.CHECKED_IN ||
                room.getRoomStatus() == RoomStatus.BOOKED) {
            throw new RoomDomainException("Cannot delete room with status " + room.getRoomStatus());
        }
    }

    @Override
    public Room setRoomVacant(Room room) {
        room.setRoomStatus(RoomStatus.VACANT);
        return room;
    }

    @Override
    public Room setRoomBooked(Room room) {
        room.setBookedRoomStatus();
        return room;
    }

    @Override
    public Room setRoomOccupied(Room room) {
        room.setOccupiedRoomStatus();
        return room;
    }

    @Override
    public Room setRoomCleaning(Room room) {
        room.setCleanRoomStatus();
        return room;
    }

    @Override
    public Room setRoomMaintenance(Room room) {
        room.setMaintenanceRoomStatus();
        return room;
    }

    @Override
    public void requestInventoryItemForRoom(Room room, InventoryItemId inventoryItemId, int quantity, StaffId requestedByStaffId) {
        if (quantity <= 0) {
            throw new RoomDomainException("Requested quantity must be positive");
        }
    }
}