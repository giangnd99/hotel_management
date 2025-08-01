package com.poly.booking.management.domain.mapper;

import com.poly.booking.management.domain.dto.RoomDto;
import com.poly.booking.management.domain.dto.request.CreateBookingCommand;
import com.poly.booking.management.domain.dto.response.BookingCreatedResponse;
import com.poly.booking.management.domain.entity.Room;
import com.poly.booking.management.domain.event.BookingCreatedEvent;
import com.poly.booking.management.domain.event.BookingPaidEvent;
import com.poly.booking.management.domain.outbox.model.room.BookingApprovalEventPayload;
import com.poly.domain.dto.response.room.RoomResponse;
import com.poly.domain.valueobject.Money;
import com.poly.domain.valueobject.RoomId;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookingDataMapper {
    public BookingApprovalEventPayload orderPaidEventToOrderApprovalEventPayload(BookingPaidEvent domainEvent) {
        return null;
    }

    public List<Room> roomsDtoToRooms(List<RoomDto> roomsDto) {
        return roomsDto.stream().map(
                roomDto ->
                        new Room(new RoomId(roomDto.getRoomId()),
                                roomDto.getRoomNumber(),
                                Money.from(roomDto.getBasePrice()),
                                roomDto.getStatus())
        ).toList();
    }

    public BookingCreatedResponse bookingCreatedEventToBookingCreatedResponse(BookingCreatedEvent bookingCreatedEvent, CreateBookingCommand createBookingCommand) {
        return BookingCreatedResponse.builder()
                .bookingId(bookingCreatedEvent.getBooking().getId().getValue())
                .customerId(bookingCreatedEvent.getBooking().getCustomerId().getValue())
                .rooms(bookingCreatedEvent.getBooking().getRooms())
                .checkInDate(bookingCreatedEvent.getBooking().getCheckInDate().getValue())
                .checkOutDate(bookingCreatedEvent.getBooking().getCheckOutDate().getValue())
                .numberOfGuests(createBookingCommand.getNumberOfGuests())
                .totalAmount(bookingCreatedEvent.getBooking().getTotalPrice().getAmount())
                .bookingDate(bookingCreatedEvent.getCreatedAt().getValue())
                .status(bookingCreatedEvent.getBooking().getStatus())
                .build();
    }
}
