package com.poly.booking.management.domain.entity;

import com.poly.booking.management.domain.valueobject.BookingRoomId;
import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.Money;
import com.poly.domain.valueobject.StaffId;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingRoom extends BaseEntity<BookingRoomId> {

    private Room room;
    private Booking booking;
    private Money price;
}
