package com.poly.room.management.dao.room.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.OneToOne;
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
    @OneToOne
    private RoomTypeEntity roomType;

    @Id
    @OneToOne
    private FurnitureEntity furniture;

    private int requireQuantity;
}
