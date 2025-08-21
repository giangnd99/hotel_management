package com.poly.booking.management.dao.room.entity;

import com.poly.booking.management.dao.booking.entity.BookingRoomEntity;
import com.poly.domain.valueobject.RoomStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "rooms")
@Entity
public class RoomEntity {

    @Id
    @Column(columnDefinition = "uuid",updatable = false)
    private UUID id;

    private String roomNumber;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "room_status")
    private RoomStatus status;

}
