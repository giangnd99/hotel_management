package com.poly.booking.management.domain.port.in.service.command;

import com.poly.booking.management.domain.dto.request.CreateBookingCommand;
import com.poly.booking.management.domain.dto.response.BookingCreatedResponse;
import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.entity.Room;
import com.poly.booking.management.domain.event.BookingCreatedEvent;
import com.poly.booking.management.domain.exception.BookingDomainException;
import com.poly.booking.management.domain.mapper.BookingDataMapper;
import com.poly.booking.management.domain.mapper.CustomerDataMapper;
import com.poly.booking.management.domain.mapper.PaymentDataMapper;
import com.poly.booking.management.domain.mapper.RoomDataMapper;
import com.poly.booking.management.domain.outbox.scheduler.payment.PaymentOutboxHelper;
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

    private final BookingRepository bookingRepository;
    private final RoomClient roomClient;
    private final RoomDataMapper roomDataMapper;
    private final CustomerDataMapper customerDataMapper;
    private final PaymentDataMapper paymentDataMapper;
    private final BookingDataMapper bookingDataMapper;
    private final BookingSagaHelper bookingSagaHelper;
    private final PaymentOutboxHelper paymentOutboxHelper;
    private final BookingDomainService bookingDomainService;

    @Transactional
    public BookingCreatedResponse createBooking(CreateBookingCommand createBookingCommand) {

        checkCustomer(createBookingCommand.getCustomerId());

        List<Room> allRooms = getRooms();

        List<Room> bookedRooms = roomDataMapper.roomsDtoToRooms(createBookingCommand.getRooms());

        Booking booking = Booking.Builder.builder()
                .customerId(new CustomerId(UUID.fromString(createBookingCommand.getCustomerId())))
                .rooms(bookedRooms)
                .checkInDate(DateCustom.of(createBookingCommand.getCheckInDate()))
                .checkOutDate(DateCustom.of(createBookingCommand.getCheckOutDate()))
                .build();

        BookingCreatedEvent bookingCreatedEvent =
                bookingDomainService.validateAndInitiateBooking(booking, allRooms);

        saveBooking(bookingCreatedEvent.getBooking());

        paymentOutboxHelper.savePaymentOutboxMessage(
                paymentDataMapper.bookingCreatedEventToRoomBookingEventPayload(bookingCreatedEvent),
                bookingCreatedEvent.getBooking().getStatus(),
                bookingSagaHelper.bookingStatusToSagaStatus(bookingCreatedEvent.getBooking().getStatus()),
                OutboxStatus.STARTED,
                UUID.randomUUID());

        return bookingDataMapper.bookingCreatedEventToBookingCreatedResponse(bookingCreatedEvent,
                createBookingCommand);
    }

    private List<Room> getRooms() {
        List<RoomResponse> allRooms = roomClient.getAllRooms().getBody();
        if (allRooms == null) {
            log.error("Could not get all rooms!");
            throw new BookingDomainException("Could not get all rooms! Please check the server status and try again later!");
        }
        return allRooms.stream().map(roomResponse ->
                new Room(new RoomId(UUID.fromString(roomResponse.getId())), roomResponse.getRoomNumber(),
                        Money.from(roomResponse.getRoomType().getBasePrice()),
                        ERoomStatus.valueOf(roomResponse.getRoomStatus()))).toList();
    }

    private void checkCustomer(String customerId) {
        if (customerId == null) {
            log.error("Customer id is null!");
            throw new BookingDomainException("Customer id is null! Please check the server status and try again later!");
        }
        if (customerId.isBlank()) {
            log.error("Customer id is blank!");
            throw new BookingDomainException("Customer id is blank! Please check the server status and try again later!");
        }
        if (!UUID.fromString(customerId).toString().equals(customerId)) {
            log.error("Customer id is not a valid UUID!");
        }
    }

    void saveBooking(Booking booking) {
        Booking savedBooking = bookingRepository.save(booking);
        if (savedBooking == null) {
            log.error("Could not save booking with id: {}! Please check the server status and try again later!", booking.getId().getValue());
            throw new BookingDomainException("Could not save booking! Please check the server status and try again later!");
        }
        log.info("Booking with id: {} has been created successfully!", savedBooking.getId().getValue());
    }
}
