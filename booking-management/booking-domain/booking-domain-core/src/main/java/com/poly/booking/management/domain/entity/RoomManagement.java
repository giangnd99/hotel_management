package com.poly.booking.management.domain.entity;

import com.poly.booking.management.domain.exception.BookingDomainException;
import com.poly.domain.valueobject.Money;
import com.poly.domain.valueobject.RoomId;

import java.util.*;

public class RoomManagement {

    private List<Room> rooms;

    public RoomManagement(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Room> setRoomsInformation(List<RoomId> requestedRooms) {
        HashMap<RoomId, Room> roomMapById = getRoomMapById();

        Set<RoomId> availableRooms = new HashSet<>();

        List<Room> updatedRooms = new ArrayList<>();
        requestedRooms.forEach(
                bookingRoomId -> {
                    Room roomInHotel = roomMapById.get(bookingRoomId);
                    if (roomInHotel != null
                            && !availableRooms.contains(bookingRoomId)
                            && roomInHotel.checkAvailableRoom()) {
                        availableRooms.add(bookingRoomId);
                        roomInHotel.updateBookedRoom(
                                roomInHotel.getRoomNumber(),
                                roomInHotel.getBasePrice());
                        updatedRooms.add(roomInHotel);
                    }
                }
        );
        if (availableRooms.isEmpty()) {
            throw new BookingDomainException("No room found");
        }
        if (availableRooms.size() == requestedRooms.size()) {
            return updatedRooms;
        }
        throw new BookingDomainException("Not enough room available");

    }

    private HashMap<RoomId, Room> getRoomMapById() {
        return rooms.stream().collect(
                HashMap::new,
                (map, value) -> map.put(value.getId(), value),
                HashMap::putAll);
    }

    public Money getTotalCost(Booking booking) {
        Money totalCost = Money.ZERO;
        List<Room> rooms = booking.getBookingRooms()
                .stream()
                .map(BookingRoom::getRoom).toList();
        for (Room room : rooms) {
            totalCost = totalCost.add(room.getBasePrice());
        }
        return totalCost;
    }

    public List<Room> getRooms() {
        return rooms;
    }
}
