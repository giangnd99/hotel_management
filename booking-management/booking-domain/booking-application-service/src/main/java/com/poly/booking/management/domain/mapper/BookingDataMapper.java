package com.poly.booking.management.domain.mapper;

import com.poly.booking.management.domain.dto.RoomDto;
import com.poly.booking.management.domain.dto.request.CreateBookingCommand;
import com.poly.booking.management.domain.dto.response.BookingCreatedResponse;
import com.poly.booking.management.domain.entity.Room;
import com.poly.booking.management.domain.event.BookingCreatedEvent;
import com.poly.booking.management.domain.event.BookingEvent;
import com.poly.booking.management.domain.event.BookingPaidEvent;
import com.poly.booking.management.domain.outbox.model.payment.BookingPaymentEventPayload;
import com.poly.booking.management.domain.outbox.model.room.BookingReservedEventPayload;
import com.poly.domain.valueobject.Money;
import com.poly.domain.valueobject.RoomId;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookingDataMapper {
    public BookingReservedEventPayload orderPaidEventToOrderApprovalEventPayload(BookingPaidEvent domainEvent) {
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

    public BookingPaymentEventPayload bookingEventToRoomBookingEventPayload(BookingEvent bookingCreatedEvent) {
        return BookingPaymentEventPayload.builder()
                .bookingId(bookingCreatedEvent.getBooking().getId().getValue().toString())
                .customerId(bookingCreatedEvent.getBooking().getCustomerId().getValue().toString())
                .paymentBookingStatus(bookingCreatedEvent.getBooking().getStatus().toString())
                .price(bookingCreatedEvent.getBooking().getTotalPrice().getAmount())
                .createdAt(bookingCreatedEvent.getCreatedAt().getValue())
                .build();
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

    public BookingReservedEventPayload bookingEventToRoomBookingEventPayload(BookingPaidEvent domainEvent) {
        return BookingReservedEventPayload.builder()
                .bookingId(domainEvent.getBooking().getId().getValue().toString())
                .customerId(domainEvent.getBooking().getCustomerId().getValue().toString())
                .roomBookingStatus(domainEvent.getBooking().getStatus().toString())
                .price(domainEvent.getBooking().getTotalPrice().getAmount())
                .createdAt(domainEvent.getCreatedAt().getValue())
                .build();
    }
}
