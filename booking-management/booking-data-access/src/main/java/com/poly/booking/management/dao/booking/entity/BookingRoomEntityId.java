package com.poly.booking.management.dao.booking.entity;

import com.poly.booking.management.dao.room.entity.RoomEntity;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingRoomEntityId implements Serializable {

    private UUID id;

    private BookingEntity booking;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingRoomEntityId that = (BookingRoomEntityId) o;
        return id.equals(that.id) && booking.equals(that.booking);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, booking);
    }
}
