package com.poly.booking.management.dao.booking.mapper;

import com.poly.booking.management.dao.booking.entity.BookingEntity;
import com.poly.booking.management.dao.booking.entity.BookingRoomEntity;
import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.entity.Customer;
import com.poly.booking.management.domain.entity.Room;
import com.poly.booking.management.domain.valueobject.TrackingId;
import com.poly.domain.valueobject.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class BookingDataAccessMapper {

    private final BookingRoomDataAccessMapper bookingRoomDataAccessMapper;

    public Booking toDomainEntity(BookingEntity bookingEntity) {

        List<BookingRoomEntity> bookingRooms = bookingEntity.getBookingRooms();

        return Booking.Builder.builder()
                .id(new BookingId(bookingEntity.getId()))
                .customer(customerEntityToCustomer(bookingEntity.getCustomerId()))
                .checkInDate(DateCustom.of(bookingEntity.getCheckIn()))
                .checkOutDate(DateCustom.of(bookingEntity.getCheckOut()))
                .totalPrice(new Money(bookingEntity.getTotalPrice()))
                .status(BookingStatus.valueOf(bookingEntity.getStatus()))
                .bookingRooms(bookingRooms.stream().map(bookingRoomDataAccessMapper::toDomainEntity).toList())
                .actualCheckInDate(bookingEntity.getActualCheckIn() != null ? DateCustom.of(bookingEntity.getActualCheckIn()) : null)
                .actualCheckOutDate(bookingEntity.getActualCheckOut() != null ? DateCustom.of(bookingEntity.getActualCheckOut()) : null)
                .trackingId(new TrackingId(bookingEntity.getTrackingId()))
                .build();
    }
//lam mapper cho list bookingRoomEntity->> BookingRoom ngay tai day khong dung BookingRoomDataAccessMapper gay may du lie
    private Room roomEntityToRoom(com.poly.booking.management.dao.room.entity.RoomEntity roomEntity) {
        return Room.Builder.builder()
                .id(new RoomId(roomEntity.getId()))
                .roomNumber(roomEntity.getRoomNumber())
                .basePrice(new Money(roomEntity.getPrice()))
                .status((roomEntity.getStatus()))
                .build();
    }

    private Customer customerEntityToCustomer(UUID customerId) {
        return Customer.Builder.builder()
                .id(new CustomerId(customerId))
                .build();
    }

    public BookingEntity toEntity(Booking booking) {
        BookingEntity bookingEntity = BookingEntity.builder()
                .id(booking.getId().getValue())
                .customerId(booking.getCustomer().getId().getValue())
                .checkIn(booking.getCheckInDate().getValue())
                .checkOut(booking.getCheckOutDate().getValue())
                .actualCheckIn(booking.getActualCheckInDate() != null ? booking.getActualCheckInDate().getValue() : null)
                .actualCheckOut(booking.getActualCheckOutDate() != null ? booking.getActualCheckOutDate().getValue() : null)
                .totalPrice(booking.getTotalPrice().getAmount())
                .status(booking.getStatus().name())
                .trackingId(booking.getTrackingId().getValue())
                .bookingRooms(booking.getBookingRooms().stream()
                        .map(bookingRoomDataAccessMapper::toRoomEntity)
                        .toList())
                .build();

        if (bookingEntity.getBookingRooms() != null) {
            bookingEntity.getBookingRooms().forEach(bookingRoomEntity -> {
                bookingRoomEntity.setBooking(bookingEntity);
            });
        }

        return bookingEntity;
    }

}
