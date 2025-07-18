package com.poly.booking.management.domain.entity;

import com.poly.booking.management.domain.valueobject.BookingRoomId;
import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.StaffId;

public class BookingRoom extends BaseEntity<BookingRoomId> {

    private StaffId staffId;
    private Room room;

    public BookingRoom(StaffId staffId, Room room) {
        this.staffId = staffId;
        this.room = room;
    }

    public void setStaffId(StaffId staffId) {
        this.staffId = staffId;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }

    public StaffId getStaffId() {
        return staffId;
    }
}
