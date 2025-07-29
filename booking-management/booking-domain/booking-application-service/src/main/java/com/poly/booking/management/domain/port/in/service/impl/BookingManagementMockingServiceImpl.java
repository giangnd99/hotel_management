package com.poly.booking.management.domain.port.in.service.impl;

import com.poly.booking.management.domain.dto.BookingDto;
import com.poly.booking.management.domain.dto.CreateBookingCommand;
import com.poly.booking.management.domain.dto.RoomDto;
import com.poly.booking.management.domain.dto.RoomSearchQuery;
import com.poly.booking.management.domain.port.in.service.BookingManagementService;
import com.poly.domain.valueobject.EBookingStatus;
import com.poly.domain.valueobject.ERoomStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingManagementMockingServiceImpl implements BookingManagementService {
    // --- Hardcoded Mock Data Instances ---
    // These instances are pre-defined and will be returned by the service methods.
    // In a real application, these would come from a database via repositories.

    // Mock Room DTOs
    private static final List<RoomDto> MOCK_ROOM_DTOS = new ArrayList<>();
    static {
        MOCK_ROOM_DTOS.add(new RoomDto(1L, "101", 100L, "Standard", "Basic room.", ERoomStatus.VACANT, 1, 2, new BigDecimal("100.00")));
        MOCK_ROOM_DTOS.add(new RoomDto(2L, "102", 100L, "Standard", "Basic room.", ERoomStatus.VACANT, 1, 2, new BigDecimal("100.00")));
        MOCK_ROOM_DTOS.add(new RoomDto(3L, "201", 200L, "Deluxe", "Spacious room.", ERoomStatus.VACANT, 2, 3, new BigDecimal("150.00")));
        MOCK_ROOM_DTOS.add(new RoomDto(4L, "202", 200L, "Deluxe", "Spacious room.", ERoomStatus.VACANT, 2, 3, new BigDecimal("150.00")));
        MOCK_ROOM_DTOS.add(new RoomDto(5L, "301", 300L, "Suite", "Luxury suite.", ERoomStatus.VACANT, 3, 4, new BigDecimal("250.00")));
    }

    // Mock Booking DTOs
    private static final List<BookingDto> MOCK_BOOKING_DTOS = new ArrayList<>();
    static {
        MOCK_BOOKING_DTOS.add(new BookingDto(
                UUID.fromString("c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a01"), 1L, "Nguyen Dang Giang", 1L, "101",
                LocalDate.of(2025, 8, 1), LocalDate.of(2025, 8, 5), 2,
                new BigDecimal("400.00"), LocalDateTime.of(2025, 7, 20, 10, 0), EBookingStatus.CONFIRMED, "Early check-in"
        ));
        MOCK_BOOKING_DTOS.add(new BookingDto(
                UUID.fromString("c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a02"), 2L, "Nguyen Phan Lam Hung", 3L, "201",
                LocalDate.of(2025, 9, 10), LocalDate.of(2025, 9, 12), 3,
                new BigDecimal("300.00"), LocalDateTime.of(2025, 7, 25, 11, 30), EBookingStatus.PENDING, "Late check-out"
        ));
        MOCK_BOOKING_DTOS.add(new BookingDto(
                UUID.fromString("c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a03"), 1L, "Nguyen Dang Giang", 5L, "301",
                LocalDate.of(2025, 10, 1), LocalDate.of(2025, 10, 7), 4,
                new BigDecimal("1500.00"), LocalDateTime.of(2025, 7, 28, 14, 0), EBookingStatus.CONFIRMED, "High floor"
        ));
        MOCK_BOOKING_DTOS.add(new BookingDto(
                UUID.fromString("c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a04"), 3L, "Nguyen Dung Hai Thach", 2L, "102",
                LocalDate.of(2025, 8, 15), LocalDate.of(2025, 8, 17), 1,
                new BigDecimal("200.00"), LocalDateTime.of(2025, 7, 29, 9, 0), EBookingStatus.CHECKED_IN, ""
        ));
        MOCK_BOOKING_DTOS.add(new BookingDto(
                UUID.fromString("c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a05"), 4L, "Nguyen Dam Hoang Linh", 4L, "202",
                LocalDate.of(2025, 9, 5), LocalDate.of(2025, 9, 8), 2,
                new BigDecimal("450.00"), LocalDateTime.of(2025, 7, 30, 16, 0), EBookingStatus.CANCELLED, "Customer illness"
        ));
    }

    /**
     * Creates a new booking. For mock purposes, it always returns a predefined booking DTO.
     * In a real scenario, this would persist the booking and return the actual created object.
     *
     * @param command The command object containing details for creating the booking.
     * @return A hardcoded BookingDto.
     */
    @Override
    public BookingDto createBooking(CreateBookingCommand command) {
        // In a real application, you'd process the command, create a Booking entity,
        // save it via a repository, and then map it to a BookingDto.
        // For this mock, we return a fixed new instance.
        return new BookingDto(
                UUID.fromString("c0eebc99-9c0b-4ef8-bb6d-6bb9bd380b01"), // A unique ID for this 'new' booking
                command.getCustomerId() != null ? command.getCustomerId() : 99L, // Use command customer ID or a mock one
                "New Mock Customer", // Mock customer name
                command.getRoomId() != null ? command.getRoomId() : 99L, // Use command room ID or a mock one
                "999", // Mock room number
                command.getCheckInDate() != null ? command.getCheckInDate() : LocalDate.now().plusDays(1),
                command.getCheckOutDate() != null ? command.getCheckOutDate() : LocalDate.now().plusDays(3),
                command.getNumberOfGuests() != 0 ? command.getNumberOfGuests() : 2,
                new BigDecimal("200.00"), // Mock total amount
                LocalDateTime.now(),
                EBookingStatus.PENDING,
                command.getSpecialRequests() != null ? command.getSpecialRequests() : "Mock special requests"
        );
    }

    /**
     * Searches for available rooms based on the provided query.
     * This mock implementation returns a fixed list of 5 RoomDto instances,
     * regardless of the query parameters.
     *
     * @param query The query object containing criteria for room search.
     * @return A list of 5 hardcoded RoomDto instances.
     */
    @Override
    public List<RoomDto> searchAvailableRooms(RoomSearchQuery query) {
        // In a real application, this would query the RoomRepository based on availability logic.
        // For this mock, we return the predefined mock list.
        return MOCK_ROOM_DTOS;
    }

    /**
     * Retrieves a booking by its ID.
     * This mock implementation always returns the first booking DTO from the mock list if any ID is provided,
     * or an empty optional if the ID is null or not found in the initial hardcoded set.
     *
     * @param bookingId The UUID of the booking to retrieve.
     * @return An Optional containing a hardcoded BookingDto, or empty if not matched.
     */
    @Override
    public Optional<Object> getBookingById(UUID bookingId) {
        // In a real application, this would query the BookingRepository.
        // For this mock, we try to find a match or return a default mock booking.
        return MOCK_BOOKING_DTOS.stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .map(b -> (Object) b) // Cast to Object as per interface
                .findFirst()
                .or(() -> {
                    // If not found, return a generic mock booking (only 1 instance for this case)
                    // You can choose to return an empty Optional here if strict "not found" is preferred.
                    if (bookingId != null) {
                        return Optional.of(new BookingDto(
                                bookingId,
                                99L, "Generic Mock Customer", 99L, "XXX",
                                LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 3), 2,
                                new BigDecimal("300.00"), LocalDateTime.now(), EBookingStatus.CONFIRMED, "Generic Request"
                        ));
                    }
                    return Optional.empty();
                });
    }

    /**
     * Retrieves all bookings.
     * This mock implementation returns a fixed list of 5 BookingDto instances.
     *
     * @return A list of 5 hardcoded BookingDto instances.
     */
    @Override
    public List<BookingDto> getAllBookings() {
        // In a real application, this would fetch all bookings from the repository.
        // For this mock, we return the predefined mock list.
        return MOCK_BOOKING_DTOS;
    }

    /**
     * Retrieves bookings associated with a specific customer ID.
     * This mock implementation filters the hardcoded list based on the customer ID,
     * ensuring that up to 5 relevant bookings are returned.
     *
     * @param customerId The ID of the customer whose bookings are to be retrieved.
     * @return A list of hardcoded BookingDto instances relevant to the customer.
     */
    @Override
    public List<BookingDto> getBookingsByCustomerId(Long customerId) {
        // In a real application, this would query the BookingRepository by customer ID.
        // For this mock, we filter our static list.
        return MOCK_BOOKING_DTOS.stream()
                .filter(booking -> booking.getCustomerId().equals(customerId))
                .limit(5) // Ensure we return at most 5 instances as per requirement
                .collect(Collectors.toList());
    }

    /**
     * Cancels a booking by its ID.
     * This mock implementation simulates cancellation by returning a hardcoded cancelled booking DTO
     * if the ID matches one of the initial mock bookings.
     *
     * @param bookingId The UUID of the booking to cancel.
     * @return A hardcoded BookingDto representing the cancelled booking, or throws an exception if not found.
     */
    @Override
    public BookingDto cancelBooking(UUID bookingId) {
        // In a real application, this would update the booking status in the repository.
        // For this mock, we return a pre-defined cancelled booking if the ID matches,
        // otherwise, we'll throw an exception for simplicity.
        return MOCK_BOOKING_DTOS.stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .map(originalBooking -> new BookingDto(
                        originalBooking.getBookingId(),
                        originalBooking.getCustomerId(),
                        originalBooking.getCustomerName(),
                        originalBooking.getRoomId(),
                        originalBooking.getRoomNumber(),
                        originalBooking.getCheckInDate(),
                        originalBooking.getCheckOutDate(),
                        originalBooking.getNumberOfGuests(),
                        originalBooking.getTotalAmount(),
                        originalBooking.getBookingDate(),
                        EBookingStatus.CANCELLED, // Set status to CANCELLED
                        originalBooking.getSpecialRequests() + " (Cancelled)"
                ))
                .orElseThrow(() -> new IllegalArgumentException("Booking not found for cancellation: " + bookingId));
    }

    /**
     * Updates details of an existing booking.
     * This mock implementation returns a hardcoded updated booking DTO
     * if the ID matches one of the initial mock bookings, otherwise throws an exception.
     *
     * @param bookingId The UUID of the booking to update.
     * @param newStatus The new status for the booking (can be null).
     * @param specialRequests The updated special requests (can be null).
     * @param numberOfGuests The updated number of guests (can be null).
     * @return A hardcoded BookingDto representing the updated booking, or throws an exception if not found.
     */
    @Override
    public BookingDto updateBookingDetails(UUID bookingId, EBookingStatus newStatus, String specialRequests, Integer numberOfGuests) {
        // In a real application, this would fetch, modify, and save the booking entity.
        // For this mock, we return a modified version of an existing mock booking if found.
        return MOCK_BOOKING_DTOS.stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .map(originalBooking -> new BookingDto(
                        originalBooking.getBookingId(),
                        originalBooking.getCustomerId(),
                        originalBooking.getCustomerName(),
                        originalBooking.getRoomId(),
                        originalBooking.getRoomNumber(),
                        originalBooking.getCheckInDate(),
                        originalBooking.getCheckOutDate(),
                        numberOfGuests != null ? numberOfGuests : originalBooking.getNumberOfGuests(),
                        originalBooking.getTotalAmount(), // For simplicity, amount not recalculated
                        originalBooking.getBookingDate(),
                        newStatus != null ? newStatus : originalBooking.getStatus(),
                        specialRequests != null ? specialRequests : originalBooking.getSpecialRequests()
                ))
                .orElseThrow(() -> new IllegalArgumentException("Booking not found for update: " + bookingId));
    }
}
