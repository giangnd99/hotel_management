package com.poly.paymentdomain.model.entity.valueobject;

import com.poly.paymentdomain.model.exception.InvalidValueException;
import lombok.Getter;

import java.util.UUID;

@Getter
public class BookingId {
    private final UUID value;

    public BookingId(UUID value) {
        if (value == null) throw new InvalidValueException("BookingId");
        this.value = value;
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
