package com.poly.room.management.dao.room.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "furniture")
public class FurnitureEntity {

    @Id
    private Integer furnitureId;

    private String name;

    private BigDecimal price;

    @OneToMany(mappedBy = "furniture", cascade = CascadeType.ALL)
    private List<RoomTypeFurnitureEntity> roomTypeFurnitures;
}
