package com.poly.room.management.domain.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.ERoomStatus;
import com.poly.domain.valueobject.RoomId;
import com.poly.room.management.domain.exception.RoomDomainException;

public class Room extends BaseEntity<RoomId> {

    private String roomNumber;

    private int floor;

    private RoomType roomType;

    private RoomStatus roomStatus;

    private Room(Builder builder) {
        super.setId(builder.id);
        roomNumber = builder.roomNumber;
        floor = builder.floor;
        roomType = builder.roomType;
        roomStatus = builder.roomStatus;
        validate();
    }

    public void validate() {
        validateRoomNumber();
        validateRoomTypeAndStatus();
    }

    public void setVacantRoomStatus() {
        if (roomStatus.getRoomStatus().equals(ERoomStatus.OCCUPIED.name())) {
            throw new RoomDomainException("Room is occupied");
        } else if (roomStatus.getRoomStatus().equals(ERoomStatus.BOOKED.name())) {
            throw new RoomDomainException("Room is already booked");
        }
        this.roomStatus = new RoomStatus(ERoomStatus.VACANT);
    }

    public void setBookedRoomStatus() {
        if (this.roomStatus.getRoomStatus().equals(ERoomStatus.MAINTENANCE.name())) {
            throw new RoomDomainException("Room maintenance");
        } else if (roomStatus.getRoomStatus().equals(ERoomStatus.OCCUPIED.name())) {
            throw new RoomDomainException("Room is already checkin");
        } else if (!this.roomStatus.getRoomStatus().equals(ERoomStatus.VACANT.name())) {
            throw new RoomDomainException("Room must vacant before booking");
        }
        this.roomStatus = new RoomStatus(ERoomStatus.BOOKED);
    }

    public void setMaintenanceRoomStatus() {
        if (roomStatus.getRoomStatus().equals(ERoomStatus.OCCUPIED.name())) {
            throw new RoomDomainException("Room is already checkin");
        }
        this.roomStatus = new RoomStatus(ERoomStatus.MAINTENANCE);
    }

    public void setCleanRoomStatus() {
        if (roomStatus.getRoomStatus().equals(ERoomStatus.OCCUPIED.name())) {
            throw new RoomDomainException("Room is already checkin");
        }
        this.roomStatus = new RoomStatus(ERoomStatus.CLEANING);
    }

    public void setOccupiedRoomStatus() {
        if (this.roomStatus.getRoomStatus().equals(ERoomStatus.MAINTENANCE.name())) {
            throw new RoomDomainException("Room cannot be occupied when it is under maintenance.");
        }
        if (this.roomStatus.getRoomStatus().equals(ERoomStatus.CLEANING.name())) {
            throw new RoomDomainException("Room cannot be occupied when it is being cleaned.");
        }
        if (!(this.roomStatus.getRoomStatus().equals(ERoomStatus.BOOKED.name()) || this.roomStatus.getRoomStatus().equals(ERoomStatus.VACANT.name()))) {
            throw new RoomDomainException("Room can only be occupied from BOOKED or VACANT status.");
        }
        this.roomStatus = new RoomStatus(ERoomStatus.OCCUPIED);
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public int getFloor() {
        return floor;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public RoomStatus getRoomStatus() {
        return roomStatus;
    }

    public void validateRoomNumber() {
        if (roomNumber == null || roomNumber.trim().isEmpty()) {
            throw new RoomDomainException("Room number cannot be empty.");
        }
    }

    private void validateRoomTypeAndStatus() {
        if (roomType == null) {
            throw new RoomDomainException("RoomType cannot be null for a Room.");
        }
        if (roomStatus == null) {
            throw new RoomDomainException("RoomStatus cannot be null for a Room.");
        }
    }

    public void setRoomNumber(String newRoomNumber) {
        this.roomNumber = newRoomNumber;
        validateRoomNumber();
    }

    public void setFloor(int newFloor) {
        this.floor = newFloor;
    }

    public void setRoomType(RoomType newRoomType) {
        this.roomType = newRoomType;
        validateRoomTypeAndStatus();
    }


    public static final class Builder {
        private RoomId id;
        private String roomNumber;
        private int floor;
        private RoomType roomType;
        private RoomStatus roomStatus;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(RoomId val) {
            id = val;
            return this;
        }

        public Builder roomNumber(String val) {
            roomNumber = val;
            return this;
        }

        public Builder floor(int val) {
            floor = val;
            return this;
        }

        public Builder roomStatus(RoomStatus val) {
            roomStatus = val;
            return this;
        }

        public Builder roomType(RoomType val) {
            roomType = val;
            return this;
        }

        public Room build() {
            return new Room(this);
        }
    }
}
