package com.poly.room.management.domain.service.impl;

import com.poly.domain.valueobject.ERoomStatus;
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
        newRoom.setVacantRoomStatus();
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
        if (room.getRoomStatus().getRoomStatus().equals(ERoomStatus.OCCUPIED.name()) ||
                room.getRoomStatus().getRoomStatus().equalsIgnoreCase(ERoomStatus.BOOKED.name())) {
            throw new RoomDomainException("Cannot delete room with status " + room.getRoomStatus().getRoomStatus());
        }
    }

    @Override
    public Room setRoomVacant(Room room) {
        room.setVacantRoomStatus();
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