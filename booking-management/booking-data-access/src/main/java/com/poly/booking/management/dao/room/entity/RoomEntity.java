package com.poly.booking.management.dao.room.entity;

import com.poly.booking.management.dao.booking.entity.BookingRoomEntity;
import jakarta.persistence.*;
import lombok.*;

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

    private String description;

    private int price;

    private String status;

    @OneToMany(mappedBy = "room",cascade = jakarta.persistence.CascadeType.ALL)
    List<BookingRoomEntity> bookingRooms;

    private int capacity;
}
