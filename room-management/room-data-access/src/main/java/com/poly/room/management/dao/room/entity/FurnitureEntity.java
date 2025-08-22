package com.poly.room.management.dao.room.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "furniture")
public class FurnitureEntity {

    @Id
    @Column(columnDefinition = "uuid",updatable = false)
    private UUID furnitureId;

    private String name;

    private BigDecimal price;

    @OneToMany(mappedBy = "furniture", cascade = CascadeType.ALL)
    private List<RoomTypeFurnitureEntity> roomTypeFurnitures;
}
