package com.poly.booking.management.dao.booking.entity;

import com.poly.booking.management.dao.room.entity.RoomEntity;
import jakarta.persistence.*;
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
    @ManyToOne
    @JoinColumn(name = "booking_id")
    private BookingEntity booking;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private RoomEntity roomId;
    private BigDecimal price;
}
