package com.poly.booking.management.domain.entity;

import com.poly.domain.valueobject.Money;
import com.poly.domain.valueobject.RoomId;

import java.util.*;
import java.util.stream.Collectors;

public class RoomManagement {

    private List<Room> rooms;

    public RoomManagement(List<Room> rooms) {
        this.rooms = rooms;
    }

    public void setRoomsInformation(Booking booking) {
        HashMap<RoomId, Room> roomMapById = getRoomMapById();

        Set<RoomId> availableRooms = new HashSet<>();

        booking.getBookingRooms().forEach(
                bookingRoom -> {
                    RoomId roomId = bookingRoom.getRoom().getId();
                    Room roomInHotel = roomMapById.get(bookingRoom.getId());
                    if (roomInHotel != null
                            && !availableRooms.contains(bookingRoom.getId())
                            && roomInHotel.checkAvailableRoom()) {

                        availableRooms.add(roomId);
                        bookingRoom.getRoom().updateBookedRoom(
                                roomInHotel.getRoomNumber(),
                                roomInHotel.getBasePrice());
                    }
                }
        );
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
