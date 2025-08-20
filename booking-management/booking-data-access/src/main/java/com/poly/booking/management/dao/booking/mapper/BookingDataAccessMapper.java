package com.poly.booking.management.dao.booking.mapper;

import com.poly.booking.management.dao.booking.entity.BookingEntity;
import com.poly.booking.management.dao.customer.entity.CustomerEntity;
import com.poly.booking.management.domain.dto.BookingDto;
import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.entity.Customer;
import com.poly.booking.management.domain.valueobject.TrackingId;
import com.poly.domain.valueobject.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


@Component
public class BookingDataAccessMapper {

    public Booking toDomainEntity(BookingEntity bookingEntity) {
        return Booking.Builder.builder()
                .id(new BookingId(bookingEntity.getId()))
                .customer(customerEntityToCustomer(bookingEntity.getCustomerId()))
                .checkInDate(DateCustom.of(bookingEntity.getCheckIn()))
                .checkOutDate(DateCustom.of(bookingEntity.getCheckOut()))
                .totalPrice(new Money(bookingEntity.getTotalPrice()))
                .status(BookingStatus.valueOf(bookingEntity.getStatus()))
                .actualCheckInDate(bookingEntity.getActualCheckIn() != null ? DateCustom.of(bookingEntity.getActualCheckIn()) : null)
                .actualCheckOutDate(bookingEntity.getActualCheckOut() != null ? DateCustom.of(bookingEntity.getActualCheckOut()) : null)
                .trackingId(new TrackingId(bookingEntity.getTrackingId()))
                .build();
    }

    public BookingDto toDto(Booking booking) {
        BookingDto dto = new BookingDto();
        dto.setBookingId(booking.getId().getValue());
        dto.setCustomerId(booking.getCustomer().getId().getValue());
        dto.setCustomerName(booking.getCustomer().getName());
        dto.setCustomerEmail(booking.getCustomer().getEmail());
        dto.setCheckInDate(LocalDate.from(booking.getCheckInDate().getValue()));
        dto.setCheckOutDate(LocalDate.from(booking.getCheckOutDate().getValue()));
        dto.setTotalAmount(booking.getTotalPrice().getAmount());
        dto.setStatus(booking.getStatus().name());
        dto.setCreatedAt(LocalDateTime.now()); // Có thể cần thêm trường này vào entity
        dto.setUpdatedAt(LocalDateTime.now()); // Có thể cần thêm trường này vào entity

        // Set room information if available
        if (booking.getBookingRooms() != null && !booking.getBookingRooms().isEmpty()) {
            dto.setRoomId(booking.getBookingRooms().get(0).getRoom().getId().getValue());
            dto.setRoomNumber(booking.getBookingRooms().get(0).getRoom().getRoomNumber());
            // Room type not available in current Room entity
        }

        return dto;
    }

    private Customer customerEntityToCustomer(UUID customerId) {
        return Customer.Builder.builder()
                .id(new CustomerId(customerId))
                .build();
    }

    public BookingEntity toEntity(Booking booking) {
        return BookingEntity.builder()
                .id(booking.getId().getValue())
                .customerId(booking.getCustomer().getId().getValue())
                .checkIn(booking.getCheckInDate().getValue())
                .checkOut(booking.getCheckOutDate().getValue())
                .actualCheckIn(booking.getActualCheckInDate() != null ? booking.getActualCheckInDate().getValue() : null)
                .actualCheckOut(booking.getActualCheckOutDate() != null ? booking.getActualCheckOutDate().getValue() : null)
                .totalPrice(booking.getTotalPrice().getAmount())
                .status(booking.getStatus().name())
                .trackingId(booking.getTrackingId().getValue())
                .build();
    }

    private CustomerEntity customerToCustomerEntity(Customer customer) {
        return CustomerEntity.builder()
                .id(customer.getId().getValue())
                .email(customer.getEmail())
                .username(customer.getUsername())
                .firstName(customer.getName().split(" ")[0])
                .lastName(customer.getName().split(" ")[1])
                .build();
    }
}
