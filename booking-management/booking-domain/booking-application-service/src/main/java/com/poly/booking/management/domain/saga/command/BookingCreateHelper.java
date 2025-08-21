package com.poly.booking.management.domain.saga.command;

import com.poly.booking.management.domain.dto.request.CreateBookingRequest;
import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.entity.Customer;
import com.poly.booking.management.domain.entity.Room;
import com.poly.booking.management.domain.exception.BookingDomainException;
import com.poly.booking.management.domain.mapper.RoomDataMapper;
import com.poly.booking.management.domain.port.out.client.RoomClient;
import com.poly.booking.management.domain.port.out.repository.BookingRepository;
import com.poly.booking.management.domain.port.out.repository.RoomRepository;
import com.poly.booking.management.domain.service.BookingDomainService;
import com.poly.domain.dto.response.room.RoomResponse;
import com.poly.domain.valueobject.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingCreateHelper {


    private final BookingRepository bookingRepository;
    private final RoomClient roomClient;
    private final RoomDataMapper roomDataMapper;
    private final RoomRepository roomRepository;
    private final BookingDomainService bookingDomainService;


    public List<Room> getRooms() {
        List<RoomResponse> allRooms = roomClient.getAllRooms().getBody();
        if (allRooms == null) {
            log.error("Could not get all rooms!");
            throw new BookingDomainException("Could not get all rooms! Please check the server status and try again later!");
        }
        return allRooms.stream().map(roomResponse ->
                new Room(new RoomId(UUID.fromString(roomResponse.getId())),
                        roomResponse.getRoomNumber(),
                        Money.from(roomResponse.getRoomType().getBasePrice()),
                        RoomStatus.valueOf(roomResponse.getRoomStatus()))).toList();
    }

    public void saveBooking(Booking booking) {

        Booking savedBooking = bookingRepository.save(booking);
        if (savedBooking == null) {
            log.error("Could not save booking with id: {}! Please check the server status and try again later!", booking.getId().getValue());
            throw new BookingDomainException("Could not save booking! Please check the server status and try again later!");
        }
        log.info("Booking with id: {} has been created successfully!", savedBooking.getId().getValue());
    }

    public Booking initAndValidateBookingCreatedEvent(CreateBookingRequest request, Customer customer) {
        List<RoomId> roomsRequest = request.getListRoomId().stream().map(RoomId::new).toList();
        List<Room> allRooms = getRooms();
        allRooms.forEach(roomRepository::save);
        Booking booking = Booking.Builder.builder()
                .checkInDate(DateCustom.of(LocalDateTime.of(request.getCheckInDate(), LocalTime.now())))
                .checkOutDate(DateCustom.of(LocalDateTime.of(request.getCheckOutDate(), LocalTime.now())))
                .build();
        booking.setCustomer(customer);
        bookingDomainService.validateAndInitiateBooking(booking, roomsRequest, allRooms);

        saveBooking(booking);
        return booking;
    }
}
