package com.poly.booking.management.domain.service.impl;

import com.poly.booking.management.domain.dto.BookingDto;
import com.poly.booking.management.domain.dto.BookingStatisticsDto;
import com.poly.booking.management.domain.dto.request.CreateBookingRequest;
import com.poly.booking.management.domain.dto.request.CreateCustomerCommand;
import com.poly.booking.management.domain.dto.request.UpdateBookingRequest;
import com.poly.booking.management.domain.dto.response.CustomerDto;
import com.poly.booking.management.domain.dto.response.DepositBookingResponse;
import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.entity.Customer;
import com.poly.booking.management.domain.port.out.client.CustomerClient;
import com.poly.booking.management.domain.port.out.repository.BookingRepository;
import com.poly.booking.management.domain.port.out.repository.CustomerRepository;

import com.poly.booking.management.domain.saga.create.BookingCreateHelper;
import com.poly.booking.management.domain.service.BookingService;
import com.poly.booking.management.domain.service.DepositBookingCommand;
import com.poly.domain.valueobject.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final CustomerClient customerClient;
    private final BookingCreateHelper bookingCreateHelper;

    private final DepositBookingCommand depositBookingCommand;


    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public Integer getTodayBookingCount() {
        return bookingRepository.countTodayBookings().intValue();
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getTodayBookingSuccessCount() {
        return bookingRepository.countTodayBookingsByStatus("CONFIRMED").intValue();
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getTodayBookingPendingCount() {
        return bookingRepository.countTodayBookingsByStatus("PENDING").intValue();
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getTodayBookingCancelCount() {
        return bookingRepository.countTodayBookingsByStatus("CANCELLED").intValue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getAllBookings(int page, int size) {
        List<Booking> bookings = bookingRepository.findAll(page, size);
        return bookings.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookingDto> getBookingById(UUID bookingId) {
        return bookingRepository.findById(bookingId)
                .map(this::mapToDto);
    }

    @Override
    @Transactional
    public BookingDto createBooking(CreateBookingRequest request) {

        Optional<Customer> existingCustomer = customerRepository.findByEmail(request.getCustomerEmail());

        Customer customer;
        if (existingCustomer.isPresent()) {
            customer = existingCustomer.get();
        } else {
            try {
                CustomerDto response = customerClient.getCustomerById(request.getCustomerId());
                customer = mapToCustomer(response);
                customer.setEmail(request.getCustomerEmail());
                customer.setUsername(request.getCustomerEmail());
                customer = customerRepository.save(customer);
                log.info("Found and synchronized customer with id: {}", request.getCustomerId());
            } catch (Exception e) {
                log.warn("Customer with ID {} not found in customer service, creating new profile.", request.getCustomerId());
                CreateCustomerCommand requestCustomer =
                        CreateCustomerCommand.builder()
                                .phone(request.getCustomerPhone())
                                .email(request.getCustomerEmail())
                                .sex("MALE")
                                .build();

                customerClient.createCustomer(requestCustomer);
                customer = customerRepository.findByEmail(request.getCustomerEmail()).orElseThrow(() ->
                        new RuntimeException("Customer not found after creating!"));
                log.info("New customer profile created successfully with id: {}", customer.getId().getValue());
            }
        }

        Booking booking = bookingCreateHelper.initAndValidateBookingCreatedEvent(request, customer);
        return mapToDto(booking);
    }

    private Customer mapToCustomer(CustomerDto customerDto) {

        String lastName = customerDto.getLastName() == null ? "Need to" : customerDto.getLastName();
        String firstName = customerDto.getFirstName() == null ? "Change" : customerDto.getFirstName();
        String name = lastName.concat(firstName);
        return Customer.Builder.builder()
                .lastName(customerDto.getLastName())
                .firstName(customerDto.getFirstName())
                .name(name)
                .status(customerDto.isActive() ? Customer.CustomerStatus.ACTIVE : Customer.CustomerStatus.INACTIVE)
                .id(new CustomerId(customerDto.getCustomerId()))
                .build();
    }

    @Override
    @Transactional
    public BookingDto updateBooking(UUID bookingId, UpdateBookingRequest request) {
        Booking existingBooking = bookingRepository.findById(bookingId)
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
            builder.checkInDate(DateCustom.of(request.getCheckInDate()));
        }

        if (request.getCheckOutDate() != null) {
            builder.checkOutDate(DateCustom.of(request.getCheckOutDate()));
        }

        // Build and save
        Booking updatedBooking = builder.build();
        Booking savedBooking = bookingRepository.save(updatedBooking);
        return mapToDto(savedBooking);
    }

    @Override
    @Transactional
    public void deleteBooking(UUID bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> searchBookings(UUID customerId, UUID roomId,
                                           LocalDate checkInDate, LocalDate checkOutDate, int page, int size) {
        List<Booking> bookings = bookingRepository.searchBookings(
                customerId, roomId, checkInDate, checkOutDate, page, size);
        return bookings.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> filterBookingsByStatus(String status, int page, int size) {
        List<Booking> bookings = bookingRepository.filterBookingsByStatus(status, page, size);
        return bookings.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> filterBookingsByDateRange(LocalDate fromDate, LocalDate toDate, int page, int size) {
        List<Booking> bookings = bookingRepository.filterBookingsByDateRange(fromDate, toDate, page, size);
        return bookings.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getBookingsByCustomerId(UUID customerId, int page, int size) {
        List<Booking> bookings = bookingRepository.findBookingsByCustomerId(customerId, page, size);
        return bookings.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public BookingDto cancelBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.cancelBooking();
        Booking cancelledBooking = bookingRepository.save(booking);
        return mapToDto(cancelledBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto confirmBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.confirmBooking();
        Booking confirmedBooking = bookingRepository.save(booking);
        return mapToDto(confirmedBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UUID> checkInBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setActualCheckInDate(DateCustom.now());
        Booking checkedInBooking = bookingRepository.save(booking);

        return checkedInBooking.getBookingRooms().stream().map(
                bookingRoom -> bookingRoom.getRoom().getId().getValue()).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UUID> checkOutBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.checkOut();
        Booking checkedOutBooking = bookingRepository.save(booking);
        return checkedOutBooking.getBookingRooms().stream().map(bookingRoom ->
                bookingRoom.getRoom().getId().getValue()).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public String getBookingPaymentStatus(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        return booking.getStatus().name();
    }

    @Override
    @Transactional
    public DepositBookingResponse confirmBookingPayment(UUID bookingId) {
        log.info("Confirming booking payment for booking id: {}", bookingId);
        return depositBookingCommand.depositBooking(bookingId);
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
        dto.setNumberOfGuests(booking.getNumberOfGuests() != null ? booking.getNumberOfGuests() : 1);
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
