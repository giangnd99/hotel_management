package com.poly.booking.management.domain.entity;


import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.RoomStatus;
import com.poly.domain.valueobject.Money;
import com.poly.domain.valueobject.RoomId;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class Room extends BaseEntity<RoomId> {

    private String roomNumber;
    private Money basePrice;
    private RoomStatus status;

    public Room(RoomId roomId, String roomNumber, Money basePrice, RoomStatus status) {
        super.setId(roomId);
        this.roomNumber = roomNumber;
        this.basePrice = basePrice;
        this.status = status;
    }

    private Room(Builder builder) {
        super.setId(builder.id);
        setRoomNumber(builder.roomNumber);
        setBasePrice(builder.basePrice);
        setStatus(builder.status);
    }

    public boolean checkAvailableRoom() {
        return status == RoomStatus.VACANT;
    }

    public void updateBookedRoom(String roomNumber, Money basePrice) {
        this.roomNumber = roomNumber;
        this.basePrice = basePrice;
        this.status = RoomStatus.BOOKED;
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

    public static final class Builder {
        private RoomId id;
        private String roomNumber;
        private Money basePrice;
        private RoomStatus status;

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

        public Builder basePrice(Money val) {
            basePrice = val;
            return this;
        }

        public Builder status(RoomStatus val) {
            status = val;
            return this;
        }

        public Room build() {
            return new Room(this);
        }
    }
}
