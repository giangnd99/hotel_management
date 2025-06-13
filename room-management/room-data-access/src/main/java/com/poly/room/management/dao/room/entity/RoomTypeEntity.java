package com.poly.room.management.dao.room.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RoomTypeEntity {

    @Id
    private Integer roomTypeId;

    private String typeName;

    private String description;

    private BigDecimal basePrice;

    private int maxOccupancy;
}
