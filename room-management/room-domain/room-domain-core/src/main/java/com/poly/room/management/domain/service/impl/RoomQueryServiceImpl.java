package com.poly.room.management.domain.service.impl;

import com.poly.domain.valueobject.ERoomStatus;
import com.poly.domain.valueobject.RoomId;
import com.poly.room.management.domain.entity.Room;
import com.poly.room.management.domain.entity.RoomType;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.service.sub.RoomQueryService;
import com.poly.room.management.domain.valueobject.RoomTypeId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RoomQueryServiceImpl implements RoomQueryService {
    @Override
    public Optional<Room> getRoomById(List<Room> allRooms, RoomId roomId) {
        return allRooms.stream()
                .filter(room -> room.getId().equals(roomId))
                .findFirst();
    }

    @Override
    public List<Room> getAllRooms(List<Room> allRooms) {
        return allRooms;
    }

    @Override
    public List<Room> findAvailableRooms(List<Room> allRooms, Optional<RoomTypeId> roomTypeId,
                                         Optional<Integer> minFloor, Optional<Integer> maxFloor, LocalDateTime checkInDate, LocalDateTime checkOutDate) {
        return allRooms.stream()
                .filter(room -> room.getRoomStatus().getRoomStatus().equals(ERoomStatus.VACANT.name()))
                .filter(room -> roomTypeId.map(id -> room.getRoomType().getId().equals(id)).orElse(true))
                .filter(room -> minFloor.map(floor -> room.getFloor() >= floor).orElse(true))
                .filter(room -> maxFloor.map(floor -> room.getFloor() <= floor).orElse(true))
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> getRoomsByStatus(List<Room> allRooms, String statusName) {
        try {
            return allRooms.stream()
                    .filter(room -> room.getRoomStatus().getRoomStatus().equalsIgnoreCase(statusName))
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new RoomDomainException("Invalid room status: " + statusName);
        }
    }

    @Override
    public Optional<RoomType> getRoomTypeById(List<RoomType> allRoomTypes, RoomTypeId roomTypeId) {
        return Optional.empty();
    }

    @Override
    public List<RoomType> getAllRoomTypes(List<RoomType> allRoomTypes) {
        return List.of();
    }
}