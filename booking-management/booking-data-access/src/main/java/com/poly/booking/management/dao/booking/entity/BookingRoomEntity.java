package com.poly.booking.management.dao.booking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "booking_room")
@Entity
public class BookingRoomEntity {

    @Id
    private UUID id;
    private UUID bookingId;
    private UUID roomId;
    private BigDecimal price;
}
