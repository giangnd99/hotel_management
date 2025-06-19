package com.poly.room.management.dao.room.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@IdClass(RoomTypeFurnitureEntityId.class)
public class RoomTypeFurnitureEntity {

    @Id
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "room_type_id")
    private RoomTypeEntity roomType;

    @Id
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "furniture_id")
    private FurnitureEntity furniture;

    private int requireQuantity;
}
