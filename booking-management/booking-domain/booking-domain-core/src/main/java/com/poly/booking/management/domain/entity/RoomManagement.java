package com.poly.booking.management.domain.entity;

import com.poly.domain.valueobject.Money;
import com.poly.domain.valueobject.RoomId;

import java.util.*;

public class RoomManagement {

    private List<Room> rooms;

    public RoomManagement(List<Room> rooms) {
        this.rooms = rooms;
    }

    public void setRoomsInformation(Booking booking) {
        HashMap<RoomId, Room> roomMapById = getRoomMapById();

        Set<RoomId> availableRooms = new HashSet<>();

        booking.bookingRooms.forEach(
                bookingRoom -> {
                    Room bookingTargetRoom = bookingRoom.getRoom();
                    RoomId roomId = bookingRoom.getRoom().getId();
                    Room roomInHotel = roomMapById.get(bookingRoom.getRoom().getId());
                    if (roomInHotel != null
                            && !availableRooms.contains(bookingRoom.getRoom().getId())
                            && roomInHotel.checkAvailableRoom()) {

                        availableRooms.add(roomId);
                        bookingTargetRoom.updateBookedRoom(
                                roomInHotel.getRoomNumber(),
                                roomInHotel.getBasePrice());
                    }
                }
        );
    }

    private HashMap<RoomId, Room> getRoomMapById() {
        return rooms.stream().collect(
                HashMap::new,
                (key, value) -> key.put(value.getId(), value),
                HashMap::putAll);
    }

    public Money getTotalCost(Booking booking) {
        Money totalCost = Money.ZERO;
        for (BookingRoom bookingRoom : booking.bookingRooms) {
            Room room = bookingRoom.getRoom();
            totalCost = totalCost.add(room.getBasePrice());
        }
        return totalCost;
    }

    public List<Room> getRooms() {
        return rooms;
    }
}
