package com.poly.room.management.dao.room.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class FurnitureEntity {

    @Id
    private Integer furnitureId;

    private String name;

    private BigDecimal price;

    @OneToMany(mappedBy = "furniture", cascade = CascadeType.ALL)
    private List<RoomTypeFurnitureEntity> roomTypeFurnitures;
}
