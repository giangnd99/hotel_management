package com.poly.booking.management.domain.entity;

import com.poly.booking.management.domain.valueobject.BookingRoomId;
import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.Money;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingRoom extends BaseEntity<BookingRoomId> {

    private Room room;
    private Booking booking;
    private Money price;

    private BookingRoom(Builder builder) {
        super.setId(builder.id);
        setRoom(builder.room);
        setBooking(builder.booking);
        setPrice(builder.price);
    }

    public void initialize(Booking booking, BookingRoomId bookingRoomId) {
        this.booking = booking;
        super.setId(bookingRoomId);}

    public static final class Builder {
        private BookingRoomId id;
        private Room room;
        private Booking booking;
        private Money price;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(BookingRoomId val) {
            id = val;
            return this;
        }

        public Builder room(Room val) {
            room = val;
            return this;
        }

        public Builder booking(Booking val) {
            booking = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public BookingRoom build() {
            return new BookingRoom(this);
        }
    }
}
