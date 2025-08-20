package com.poly.booking.management.domain.service.impl;

import com.poly.booking.management.domain.dto.BookingDto;
import com.poly.booking.management.domain.dto.BookingStatisticsDto;
import com.poly.booking.management.domain.dto.RoomDto;
import com.poly.booking.management.domain.dto.request.CreateBookingCommand;
import com.poly.booking.management.domain.dto.request.CreateBookingRequest;
import com.poly.booking.management.domain.dto.request.UpdateBookingRequest;
import com.poly.booking.management.domain.dto.response.BookingCreatedResponse;
import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.entity.BookingRoom;
import com.poly.booking.management.domain.entity.Customer;
import com.poly.booking.management.domain.entity.Room;
import com.poly.booking.management.domain.mapper.BookingDataMapper;
import com.poly.booking.management.domain.port.out.repository.BookingRepository;
import com.poly.booking.management.domain.port.out.repository.CustomerRepository;
import com.poly.booking.management.domain.port.out.repository.RoomRepository;

import com.poly.booking.management.domain.saga.command.BookingCreateCommendHandler;
import com.poly.booking.management.domain.service.BookingService;
import com.poly.domain.valueobject.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final RoomRepository roomRepository;
    private final BookingCreateCommendHandler bookingCreateCommendHandler;
    private final BookingDataMapper bookingDataMapper;


    @Override
    public BookingStatisticsDto getTodayBookingStatistics() {
        Long totalBookings = bookingRepository.countTodayBookings();
        Long successfulBookings = bookingRepository.countTodayBookingsByStatus("CONFIRMED");
        Long pendingBookings = bookingRepository.countTodayBookingsByStatus("PENDING");
        Long cancelledBookings = bookingRepository.countTodayBookingsByStatus("CANCELLED");
        Long checkInBookings = bookingRepository.countTodayBookingsByStatus("CHECKED_IN");
        Long checkOutBookings = bookingRepository.countTodayBookingsByStatus("CHECKED_OUT");
        Double totalRevenue = bookingRepository.getTodayTotalRevenue();
        Double averageBookingValue = bookingRepository.getTodayAverageBookingValue();

        return BookingStatisticsDto.builder()
                .totalBookings(totalBookings)
                .successfulBookings(successfulBookings)
                .pendingBookings(pendingBookings)
                .cancelledBookings(cancelledBookings)
                .checkInBookings(checkInBookings)
                .checkOutBookings(checkOutBookings)
                .totalRevenue(totalRevenue)
                .averageBookingValue(averageBookingValue)
                .build();
    }

    @Override
    public Integer getTodayBookingCount() {
        return bookingRepository.countTodayBookings().intValue();
    }

    @Override
    public Integer getTodayBookingSuccessCount() {
        return bookingRepository.countTodayBookingsByStatus("CONFIRMED").intValue();
    }

    @Override
    public Integer getTodayBookingPendingCount() {
        return bookingRepository.countTodayBookingsByStatus("PENDING").intValue();
    }

    @Override
    public Integer getTodayBookingCancelCount() {
        return bookingRepository.countTodayBookingsByStatus("CANCELLED").intValue();
    }

    @Override
    public List<BookingDto> getAllBookings(int page, int size) {
        List<Booking> bookings = bookingRepository.findAll(page, size);
        return bookings.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BookingDto> getBookingById(UUID bookingId) {
        return bookingRepository.findById(new BookingId(bookingId))
                .map(this::mapToDto);
    }

    @Override
    public BookingDto createBooking(CreateBookingRequest request) {
        // Validate customer exists
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Create new booking
        Booking booking = Booking.Builder.builder()
                .customer(customer)
                .checkInDate(DateCustom.of(request.getCheckInDate().atStartOfDay()))
                .checkOutDate(DateCustom.of(request.getCheckOutDate().atStartOfDay()))
                .status(BookingStatus.PENDING)
                .build();

        CreateBookingCommand createBookingCommand = CreateBookingCommand.builder()
                .checkInDate(LocalDateTime.of(request.getCheckInDate(), LocalTime.now()))
                .checkOutDate(LocalDateTime.of(request.getCheckOutDate(), LocalTime.now()))
                .customerId(request.getCustomerId().toString())
                .numberOfGuests(request.getNumberOfGuests())
                .rooms(getListRoomsById(request.getListRoomId(), request))
                .specialRequests(request.getSpecialRequests())
                .build();
        BookingCreatedResponse response = bookingCreateCommendHandler.createBooking(createBookingCommand);
        // Save and return
        booking.setBookingRooms(getListRoomsById(request.getListRoomId(), booking));
        booking.setTotalPrice(new Money(response.getTotalAmount()));
        booking.initiateBooking();
        Booking savedBooking = bookingRepository.save(booking);
        return mapToDto(savedBooking);
    }

    private List<RoomDto> getListRoomsById(List<UUID> roomsId, CreateBookingRequest request) {
        return roomsId.stream().map(roomRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(room ->
                        RoomDto.builder()
                                .roomId(room.getId().getValue().toString())
                                .roomNumber(room.getRoomNumber())
                                .status(room.getStatus())
                                .capacity(request.getNumberOfGuests())
                                .basePrice(room.getBasePrice().getAmount())
                                .build()).toList();
    }

    private List<BookingRoom> getListRoomsById(List<UUID> roomsId, Booking booking) {
        return roomsId.stream().map(roomRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get).map(room ->
                        BookingRoom.builder()
                                .room(room)
                                .price(room.getBasePrice())
                                .booking(booking)
                                .build()).toList();
    }

    @Override
    public BookingDto updateBooking(UUID bookingId, UpdateBookingRequest request) {
        Booking existingBooking = bookingRepository.findById(new BookingId(bookingId))
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Create a new builder with existing data
        Booking.Builder builder = Booking.Builder.builder()
                .id(existingBooking.getId())
                .customer(existingBooking.getCustomer())
                .checkInDate(existingBooking.getCheckInDate())
                .checkOutDate(existingBooking.getCheckOutDate())
                .status(existingBooking.getStatus())
                .trackingId(existingBooking.getTrackingId())
                .actualCheckInDate(existingBooking.getActualCheckInDate())
                .actualCheckOutDate(existingBooking.getActualCheckOutDate())
                .totalPrice(existingBooking.getTotalPrice())
                .bookingRooms(existingBooking.getBookingRooms());

        // Update fields if provided
        if (request.getCheckInDate() != null) {
            builder.checkInDate(DateCustom.of(request.getCheckInDate().atStartOfDay()));
        }

        if (request.getCheckOutDate() != null) {
            builder.checkOutDate(DateCustom.of(request.getCheckOutDate().atStartOfDay()));
        }

        // Build and save
        Booking updatedBooking = builder.build();
        Booking savedBooking = bookingRepository.save(updatedBooking);
        return mapToDto(savedBooking);
    }

    @Override
    public void deleteBooking(UUID bookingId) {
        bookingRepository.deleteById(new BookingId(bookingId));
    }

    @Override
    public List<BookingDto> searchBookings(String customerName, String customerEmail, String roomNumber,
                                           LocalDate checkInDate, LocalDate checkOutDate, int page, int size) {
        List<Booking> bookings = bookingRepository.searchBookings(
                customerName, customerEmail, roomNumber, checkInDate, checkOutDate, page, size);
        return bookings.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> filterBookingsByStatus(String status, int page, int size) {
        List<Booking> bookings = bookingRepository.filterBookingsByStatus(status, page, size);
        return bookings.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> filterBookingsByDateRange(LocalDate fromDate, LocalDate toDate, int page, int size) {
        List<Booking> bookings = bookingRepository.filterBookingsByDateRange(fromDate, toDate, page, size);
        return bookings.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getBookingsByCustomerId(UUID customerId, int page, int size) {
        List<Booking> bookings = bookingRepository.findBookingsByCustomerId(customerId, page, size);
        return bookings.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getCustomerBookingHistory(UUID customerId, int page, int size) {
        List<Booking> bookings = bookingRepository.findCustomerBookingHistory(customerId, page, size);
        return bookings.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
//
//    @Override
//    public List<BookingDto> searchAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate,
//                                                Integer numberOfGuests, Long roomTypeId,
//                                                Integer minPrice, Integer maxPrice) {
//        List<Booking> availableBookings = bookingRepository.findAvailableRooms(
//                checkInDate, checkOutDate, numberOfGuests, roomTypeId, minPrice, maxPrice);
//        return availableBookings.stream()
//                .map(this::mapToDto)
//                .collect(Collectors.toList());
//    }

    @Override
    public BookingDto cancelBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(new BookingId(bookingId))
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.cancelBooking();
        Booking cancelledBooking = bookingRepository.save(booking);
        return mapToDto(cancelledBooking);
    }

    @Override
    public BookingDto confirmBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(new BookingId(bookingId))
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.confirmBooking();
        Booking confirmedBooking = bookingRepository.save(booking);
        return mapToDto(confirmedBooking);
    }

    @Override
    public BookingDto checkInBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(new BookingId(bookingId))
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.checkIn();
        Booking checkedInBooking = bookingRepository.save(booking);
        return mapToDto(checkedInBooking);
    }

    @Override
    public BookingDto checkOutBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(new BookingId(bookingId))
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.checkOut();
        Booking checkedOutBooking = bookingRepository.save(booking);
        return mapToDto(checkedOutBooking);
    }

    @Override
    public String getBookingPaymentStatus(UUID bookingId) {
        Booking booking = bookingRepository.findById(new BookingId(bookingId))
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        return booking.getStatus().name();
    }

    @Override
    public BookingDto confirmBookingPayment(UUID bookingId) {
        Booking booking = bookingRepository.findById(new BookingId(bookingId))
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.paidBooking();
        Booking paidBooking = bookingRepository.save(booking);
        return mapToDto(paidBooking);
    }

    @Override
    public void sendBookingConfirmation(UUID bookingId) {
        // Implementation for sending confirmation notification
        // This would typically integrate with a notification service
        System.out.println("Sending booking confirmation for: " + bookingId);
    }

    @Override
    public void sendBookingReminder(UUID bookingId) {
        // Implementation for sending reminder notification
        // This would typically integrate with a notification service
        System.out.println("Sending booking reminder for: " + bookingId);
    }

    /**
     * Chuyển đổi từ domain entity sang DTO
     */
    private BookingDto mapToDto(Booking booking) {
        if (booking == null) {
            return null;
        }

        BookingDto dto = new BookingDto();
        dto.setBookingId(booking.getId().getValue());

        if (booking.getCustomer() != null) {
            dto.setCustomerId(booking.getCustomer().getId().getValue());
            dto.setCustomerName(booking.getCustomer().getName());
            dto.setCustomerEmail(booking.getCustomer().getEmail());
        }

        if (booking.getCheckInDate() != null) {
            dto.setCheckInDate(LocalDate.from(booking.getCheckInDate().getValue()));
        }

        if (booking.getCheckOutDate() != null) {
            dto.setCheckOutDate(LocalDate.from(booking.getCheckOutDate().getValue()));
        }

        if (booking.getTotalPrice() != null) {
            dto.setTotalAmount(booking.getTotalPrice().getAmount());
        }

        if (booking.getStatus() != null) {
            dto.setStatus(booking.getStatus().name());
        }

        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());

        // Set room information if available
        if (booking.getBookingRooms() != null && !booking.getBookingRooms().isEmpty()) {
            var firstRoom = booking.getBookingRooms().get(0);
            if (firstRoom.getRoom() != null) {
                dto.setRoomId(firstRoom.getRoom().getId().getValue());
                dto.setRoomNumber(firstRoom.getRoom().getRoomNumber());
            }
        }

        return dto;
    }
}
