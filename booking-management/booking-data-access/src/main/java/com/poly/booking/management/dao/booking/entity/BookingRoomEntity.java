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
    @Column(columnDefinition = "uuid",updatable = false)
    private UUID id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "booking_id")
    private BookingEntity booking;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    private RoomEntity room;
    private BigDecimal price;
}
