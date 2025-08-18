package com.poly.booking.management.dao.room.entity;

import com.poly.booking.management.dao.booking.entity.BookingRoomEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
    private UUID id;

    private String roomNumber;

    private String description;

    private int price;

    private String status;

    @OneToMany(mappedBy = "room",cascade = jakarta.persistence.CascadeType.ALL)
    List<BookingRoomEntity> bookingRooms;

    private int capacity;
}
