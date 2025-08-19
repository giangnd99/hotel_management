package com.poly.booking.management.domain.saga.command;

import com.poly.booking.management.domain.dto.request.CreateBookingCommand;
import com.poly.booking.management.domain.dto.response.BookingCreatedResponse;
import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.entity.BookingRoom;
import com.poly.booking.management.domain.entity.Room;
import com.poly.booking.management.domain.event.BookingCreatedEvent;
import com.poly.booking.management.domain.exception.BookingDomainException;
import com.poly.booking.management.domain.mapper.BookingDataMapper;
import com.poly.booking.management.domain.mapper.CustomerDataMapper;
import com.poly.booking.management.domain.mapper.PaymentDataMapper;
import com.poly.booking.management.domain.mapper.RoomDataMapper;
import com.poly.booking.management.domain.outbox.service.impl.PaymentOutboxImpl;
import com.poly.booking.management.domain.port.out.client.RoomClient;
import com.poly.booking.management.domain.port.out.repository.BookingRepository;
import com.poly.booking.management.domain.saga.BookingSagaHelper;
import com.poly.booking.management.domain.service.BookingDomainService;
import com.poly.domain.dto.response.room.RoomResponse;
import com.poly.domain.valueobject.*;
import com.poly.outbox.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingCreateCommendHandler {

    private final BookingCreateHelper bookingCreateHelper;

    @Transactional
    public BookingCreatedResponse createBooking(CreateBookingCommand createBookingCommand) {

        bookingCreateHelper.checkCustomer(createBookingCommand.getCustomerId());

        BookingCreatedEvent bookingCreatedEvent =
                bookingCreateHelper.initAndValidateBookingCreatedEvent(createBookingCommand);


        return bookingCreateHelper.responseDto(bookingCreatedEvent,
                createBookingCommand);
    }


}
