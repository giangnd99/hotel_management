package com.poly.booking.management.dao.booking.mapper;

import com.poly.booking.management.dao.booking.entity.BookingEntity;
import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.valueobject.TrackingId;
import com.poly.domain.valueobject.*;
import org.springframework.stereotype.Component;


@Component
public class BookingDataAccessMapper {

    public Booking toDomainEntity(BookingEntity bookingEntity) {

        return Booking.Builder.builder()
                .id(new BookingId(bookingEntity.getId()))
                .customerId(new CustomerId(bookingEntity.getCustomerId()))
                .checkInDate(DateCustom.of(bookingEntity.getCheckIn()))
                .checkOutDate(DateCustom.of(bookingEntity.getCheckOut()))
                .totalPrice(new Money(bookingEntity.getTotalPrice()))
                .status(EBookingStatus.valueOf(bookingEntity.getStatus()))
                .actualCheckInDate(DateCustom.of(bookingEntity.getActualCheckIn()))
                .actualCheckOutDate(DateCustom.of(bookingEntity.getActualCheckOut()))
                .trackingId(new TrackingId(bookingEntity.getTrackingId()))
                .build();
    }

    public BookingEntity toEntity(Booking booking) {
        return BookingEntity.builder()
                .id(booking.getId().getValue())
                .customerId(booking.getCustomerId().getValue())
                .checkIn(booking.getCheckInDate().getValue())
                .checkOut(booking.getCheckOutDate().getValue())
                .actualCheckIn(booking.getActualCheckInDate().getValue())
                .actualCheckOut(booking.getActualCheckOutDate().getValue())
                .totalPrice(booking.getTotalPrice().getAmount())
                .status(booking.getStatus().name())
                .build();
    }

}
