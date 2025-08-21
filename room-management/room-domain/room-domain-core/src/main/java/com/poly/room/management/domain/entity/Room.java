package com.poly.room.management.domain.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.Money;
import com.poly.domain.valueobject.RoomStatus;
import com.poly.domain.valueobject.RoomId;
import com.poly.room.management.domain.exception.RoomDomainException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Room extends BaseEntity<RoomId> {

    private String roomNumber;

    private int floor;

    private RoomType roomType;

    private RoomStatus roomStatus;

    private Money roomPrice;

    private String area;

    private String image_url;

    private List<RoomCost> roomCosts;

    private List<Furniture> furnitures;

    private List<RoomMaintenance> roomMaintenances;

    private Room(Builder builder) {
        super.setId(builder.id);
        roomNumber = builder.roomNumber;
        floor = builder.floor;
        roomType = builder.roomType;
        roomStatus = builder.roomStatus;
    }

    public void validate() {
        validateRoomNumber();
        validateRoomTypeAndStatus();
    }


    public void setRoomCosts(List<RoomCost> roomCosts) {
        this.roomCosts = roomCosts;
    }

    public List<RoomCost> getRoomCosts() {
        return roomCosts;
    }

    public void addRoomCost(RoomCost roomCost) {
        if (roomCosts == null) {
            roomCosts = new ArrayList<>();
        }
        roomCosts.add(roomCost);

    }

    public void setRoomStatus(RoomStatus roomStatus) {
        this.roomStatus = roomStatus;
    }

    public void addRoomMaintenance(RoomMaintenance roomMaintenance) {
        if (roomMaintenances == null) {
            roomMaintenances = new ArrayList<>();
        }
    }

    public Money updateRoomPriceAfterAddRoomCost() {
        BigDecimal totalRoomCostAmount = this.roomCosts.stream()
                .map(roomCost -> {
                    if (roomCost.getCost().getName().contains("Deposit")) {
                        return roomCost.getCost().getPrice().getAmount().negate();
                    }
                    return roomCost.getCost().getPrice().getAmount();
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Money totalRoomCost = new Money(totalRoomCostAmount);

        return this.roomPrice.add(totalRoomCost);
    }

    public void setBookedRoomStatus() {
        if (roomStatus == RoomStatus.MAINTENANCE) {
            throw new RoomDomainException("Room is maintenance");
        } else if (roomStatus == RoomStatus.CHECKED_IN) {
            throw new RoomDomainException("Room is already checkin");
        } else if (roomStatus != RoomStatus.VACANT) {
            throw new RoomDomainException("Room must vacant before booking");
        }
        this.roomStatus = RoomStatus.BOOKED;
    }

    public void setMaintenanceRoomStatus() {
        if (roomStatus == RoomStatus.CHECKED_IN) {
            throw new RoomDomainException("Room is already checkin");
        }
        this.roomStatus = RoomStatus.MAINTENANCE;
    }

    public void setCleanRoomStatus() {
        if (roomStatus == RoomStatus.CHECKED_IN) {
            throw new RoomDomainException("Room is already checkin");
        }
        this.roomStatus = RoomStatus.CLEANING;
    }

    public void setOccupiedRoomStatus() {
        if (this.roomStatus == RoomStatus.MAINTENANCE) {
            throw new RoomDomainException("Room cannot be occupied when it is under maintenance.");
        }
        if (this.roomStatus == RoomStatus.CLEANING) {
            throw new RoomDomainException("Room cannot be occupied when it is being cleaned.");
        }
        if (!(this.roomStatus == RoomStatus.BOOKED || this.roomStatus == RoomStatus.VACANT)) {
            throw new RoomDomainException("Room can only be occupied from BOOKED or VACANT status.");
        }
        this.roomStatus = RoomStatus.CHECKED_IN;
    }

    public void setCheckOutRoomStatus() {
        if (this.roomStatus == RoomStatus.CLEANING) {
            throw new RoomDomainException("Room cannot be checked out when it is being cleaned.");
        }
        if (!(this.roomStatus == RoomStatus.CHECKED_IN || this.roomStatus == RoomStatus.BOOKED)) {
            throw new RoomDomainException("Room can only be checked out from CHECKED_IN or BOOKED status.");
        }
        this.roomStatus = RoomStatus.CHECKED_OUT;
    }

    public Money getRoomPrice() {
        return getRoomType().getBasePrice();
    }

    public List<Furniture> getFurnitures() {
        return roomType.getFurnituresRequirements()
                .stream()
                .map(RoomTypeFurniture::getFurniture)
                .toList();
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

    public String getImage_url() {
        return image_url;
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
        private String image_url;
        private Money roomPrice;
        private String area;
        private List<Furniture> furnitures;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(RoomId val) {
            id = val;
            return this;
        }

        public Builder roomPrice(Money val) {
            roomPrice = val;
            return this;
        }

        public Builder area(String val) {
            area = val;
            return this;
        }

        public Builder furnitures(List<Furniture> val) {
            furnitures = val;
            return this;
        }

        public Builder image_url(String val) {
            image_url = val;
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
