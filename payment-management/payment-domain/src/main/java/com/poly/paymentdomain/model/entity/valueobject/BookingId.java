package com.poly.paymentdomain.model.entity.valueobject;

import com.poly.paymentdomain.model.exception.InvalidValueException;
import lombok.Getter;

import java.util.UUID;

@Getter
public class BookingId {
    private final UUID value;

    public BookingId(UUID value) {
        this.value = value;
    }

    public static BookingId ofNullable(UUID value) {
        return value != null ? new BookingId(value) : null;
    }

    public static BookingId of(UUID value) {
        if (value == null) {
            throw new InvalidValueException("BookingId");
        }
        return new BookingId(value);
    }

    public static BookingId from(UUID value) {
        return new BookingId(value);
    }

    public static UUID to(BookingId bookingId) {
        return UUID.fromString(bookingId.value.toString());
    }

    public UUID getValue() {
        return value;
    }
}
