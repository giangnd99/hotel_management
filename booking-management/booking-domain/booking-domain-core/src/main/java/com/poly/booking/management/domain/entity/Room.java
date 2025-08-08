package com.poly.booking.management.domain.entity;


import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.ERoomStatus;
import com.poly.domain.valueobject.Money;
import com.poly.domain.valueobject.RoomId;

public class Room extends BaseEntity<RoomId> {

    private String roomNumber;
    private Money basePrice;
    private ERoomStatus status;

    public Room(RoomId roomId, String roomNumber, Money basePrice, ERoomStatus status) {
        super.setId(roomId);
        this.roomNumber = roomNumber;
        this.basePrice = basePrice;
        this.status = status;
    }

    public boolean checkAvailableRoom() {
        return status == ERoomStatus.VACANT;
    }

    public void updateBookedRoom(String roomNumber, Money basePrice) {
        this.roomNumber = roomNumber;
        this.basePrice = basePrice;
        this.status = ERoomStatus.BOOKED;
    }

    @Override
    public String toString() {
        return String.format("Phòng %s (%s)  - Giá: %.2f", roomNumber, basePrice);
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setBasePrice(Money basePrice) {
        this.basePrice = basePrice;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public Money getBasePrice() {
        return basePrice;
    }
}
