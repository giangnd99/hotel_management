package com.poly.booking.management.dao.booking.mapper;

import com.poly.booking.management.dao.booking.entity.BookingEntity;
import com.poly.booking.management.dao.booking.entity.BookingRoomEntity;
import com.poly.booking.management.dao.room.entity.RoomEntity;

import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.entity.BookingRoom;
import com.poly.booking.management.domain.entity.Customer;
import com.poly.booking.management.domain.entity.Room;
import com.poly.booking.management.domain.valueobject.BookingRoomId;
import com.poly.booking.management.domain.valueobject.TrackingId;
import com.poly.domain.valueobject.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BookingRoomDataAccessMapper {

    public BookingRoom toDomainEntity(BookingRoomEntity bookingRoomEntity) {
        return BookingRoom.Builder.builder()
                .id(new BookingRoomId(bookingRoomEntity.getId()))
                .room(toDomain(bookingRoomEntity.getRoomId()))
                .booking(toDomain(bookingRoomEntity.getBooking()))
                .price(new Money(bookingRoomEntity.getPrice()))
                .build();
    }

    public BookingRoomEntity toRoomEntity(BookingRoom bookingRoom) {
        return BookingRoomEntity.builder()
                .id(bookingRoom.getId().getValue())
                .price(bookingRoom.getPrice().getAmount())
                .roomId(bookingRoom.getRoom().getId().getValue())
                .booking(toBookingEntity(bookingRoom.getBooking()))
                .build();
    }

    private BookingEntity toBookingEntity(Booking booking) {
        return BookingEntity.builder()
                .id(booking.getId().getValue())
                .customerId(booking.getCustomer().getId().getValue())
                .checkIn(booking.getCheckInDate().getValue())
                .checkOut(booking.getCheckOutDate().getValue())
                .actualCheckIn(booking.getActualCheckInDate() != null ? booking.getActualCheckInDate().getValue() : null)
                .actualCheckOut(booking.getActualCheckOutDate() != null ? booking.getActualCheckOutDate().getValue() : null)
                .totalPrice(booking.getTotalPrice().getAmount())
                .status(booking.getStatus().name())
                .trackingId(booking.getTrackingId().getValue())
                .build();
    }

    private RoomEntity toRoomEntity(Room room) {
        return RoomEntity.builder()
                .roomNumber(room.getRoomNumber())
                .price(room.getBasePrice().getAmount())
                .status(room.getStatus())
                .id(room.getId().getValue())
                .build();
    }


    public Room toDomain(UUID roomId) {
        return Room.Builder.builder()
                .id(new com.poly.domain.valueobject.RoomId(roomId))
                .build();
    }

    //Thu su dung booking Mapper de xem co khac khong
    //Sai o day vo khong gan lai booking room cho booking nen mat du lieu
    public Booking toDomain(BookingEntity entity) {
        return Booking.Builder.builder()
                .id(new BookingId(entity.getId()))
                .totalPrice(new com.poly.domain.valueobject.Money(entity.getTotalPrice()))
                .checkInDate(DateCustom.of(entity.getCheckIn()))
                .checkOutDate(DateCustom.of(entity.getCheckOut()))
                .actualCheckInDate(entity.getActualCheckIn() != null ? DateCustom.of(entity.getActualCheckIn()) : null)
                .actualCheckOutDate(entity.getActualCheckOut() != null ? DateCustom.of(entity.getActualCheckOut()) : null)
                .customer(Customer.Builder.builder()
                        .id(new com.poly.domain.valueobject.CustomerId(entity.getCustomerId()))
                        .build())
                .status(BookingStatus.valueOf(entity.getStatus()))
                .trackingId(new TrackingId(entity.getTrackingId()))
                .build();
    }
}
