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
public class FurnitureEntity {

    @Id
    private Integer furnitureId;

    private String name;

    private BigDecimal price;
}
