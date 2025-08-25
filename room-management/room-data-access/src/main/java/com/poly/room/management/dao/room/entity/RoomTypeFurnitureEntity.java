package com.poly.room.management.dao.room.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "room_type_furniture")
public class RoomTypeFurnitureEntity {

    @Id
    @Column(columnDefinition = "uuid",updatable = false)
    private UUID roomTypeFurnitureId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id")
    private RoomTypeEntity roomType;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "furniture_id")
    private FurnitureEntity furniture;

    private int requireQuantity;
}
