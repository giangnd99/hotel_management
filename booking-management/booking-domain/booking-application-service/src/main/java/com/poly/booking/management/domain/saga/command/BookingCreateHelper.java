package com.poly.booking.management.domain.saga.command;

import com.poly.booking.management.domain.dto.request.CreateBookingCommand;
import com.poly.booking.management.domain.dto.response.BookingCreatedResponse;
import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.entity.BookingRoom;
import com.poly.booking.management.domain.entity.Customer;
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
import com.poly.booking.management.domain.port.out.repository.CustomerRepository;
import com.poly.booking.management.domain.saga.BookingSagaHelper;
import com.poly.booking.management.domain.service.BookingDomainService;
import com.poly.domain.dto.response.room.RoomResponse;
import com.poly.domain.valueobject.*;
import com.poly.outbox.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingCreateHelper {


    private final BookingRepository bookingRepository;
    private final RoomClient roomClient;
    private final RoomDataMapper roomDataMapper;
    private final BookingDataMapper bookingDataMapper;
    private final CustomerRepository customerRepository;
    private final BookingDomainService bookingDomainService;

    public List<Room> getRooms() {
        List<RoomResponse> allRooms = roomClient.getAllRooms().getBody();
        if (allRooms == null) {
            log.error("Could not get all rooms!");
            throw new BookingDomainException("Could not get all rooms! Please check the server status and try again later!");
        }
        return allRooms.stream().map(roomResponse ->
                new Room(new RoomId(UUID.fromString(roomResponse.getId())), roomResponse.getRoomNumber(),
                        Money.from(roomResponse.getRoomType().getBasePrice()),
                        RoomStatus.valueOf(roomResponse.getRoomStatus()))).toList();
    }

    public void checkCustomer(String customerId) {
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

    public void saveBooking(Booking booking) {
        Booking savedBooking = bookingRepository.save(booking);
        if (savedBooking == null) {
            log.error("Could not save booking with id: {}! Please check the server status and try again later!", booking.getId().getValue());
            throw new BookingDomainException("Could not save booking! Please check the server status and try again later!");
        }
        log.info("Booking with id: {} has been created successfully!", savedBooking.getId().getValue());
    }

    public BookingCreatedEvent initAndValidateBookingCreatedEvent(CreateBookingCommand createBookingCommand) {
        List<Room> roomsRequest = roomDataMapper.roomsDtoToRooms(createBookingCommand.getRooms());
        List<Room> allRooms = getRooms();
        Customer customer = validateAndGetCustomer(createBookingCommand.getCustomerId());
        Booking booking = Booking.Builder.builder()
                .customer(customer)
                .checkInDate(DateCustom.of(createBookingCommand.getCheckInDate()))
                .checkOutDate(DateCustom.of(createBookingCommand.getCheckOutDate()))
                .build();

        List<BookingRoom> bookingRooms = roomDataMapper.mapRoomWithBooking(booking, roomsRequest);

        booking.setBookingRooms(bookingRooms);

        BookingCreatedEvent bookingCreatedEvent =
                bookingDomainService.validateAndInitiateBooking(booking, allRooms);

        saveBooking(bookingCreatedEvent.getBooking());

        return bookingCreatedEvent;
    }



    public BookingCreatedResponse responseDto(BookingCreatedEvent bookingCreatedEvent, CreateBookingCommand createBookingCommand) {
        return bookingDataMapper.bookingCreatedEventToBookingCreatedResponse(bookingCreatedEvent,
                createBookingCommand);
    }


    private Customer validateAndGetCustomer(String customerId) {
        Optional<Customer> customer = customerRepository.findById(UUID.fromString(customerId));
        if (customer.isEmpty()) {
            log.error("Customer with id: {} could not be found!", customerId);
            throw new BookingDomainException("Customer with id " + customerId + " could not be found!");
        }
        return customer.get();
    }
}
