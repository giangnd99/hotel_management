package com.poly.booking.management.dao.booking.entity;

import com.poly.booking.management.dao.room.entity.RoomEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "booking_room")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(BookingRoomEntityId.class)
public class BookingRoomEntity {
    @Id
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = BookingEntity.class)
    @JoinColumn(name = "booking_id")
    private BookingEntity booking;

    private UUID roomId;

    private BigDecimal price;
}
