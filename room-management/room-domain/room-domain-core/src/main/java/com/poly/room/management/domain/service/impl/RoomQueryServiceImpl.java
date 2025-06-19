package com.poly.room.management.domain.service.impl;

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
}