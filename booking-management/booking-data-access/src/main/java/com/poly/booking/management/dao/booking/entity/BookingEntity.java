package com.poly.booking.management.dao.booking.entity;

import com.poly.booking.management.domain.entity.Booking;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bookings")
@Entity
public class BookingEntity {

    @Id
    private UUID id;
    private UUID customerId;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private LocalDateTime actualCheckIn;
    private LocalDateTime actualCheckOut;
    private UUID trackingId;
    private BigDecimal totalPrice;
    private String status;
}
