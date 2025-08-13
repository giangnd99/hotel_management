package com.poly.booking.management.dao.room.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

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
}
