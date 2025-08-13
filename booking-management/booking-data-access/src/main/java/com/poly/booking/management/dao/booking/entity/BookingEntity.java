package com.poly.booking.management.dao.booking.entity;

import com.poly.booking.management.dao.room.entity.RoomEntity;
import com.poly.booking.management.domain.entity.Booking;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<BookingRoomEntity> bookingRooms;
    private UUID trackingId;
    private BigDecimal totalPrice;
    private String status;
}
