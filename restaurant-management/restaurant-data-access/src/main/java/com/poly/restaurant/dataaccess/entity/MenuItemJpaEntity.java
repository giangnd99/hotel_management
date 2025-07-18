package com.poly.restaurant.dataaccess.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemJpaEntity {
    @Id
    private Integer id;

    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private Integer quantity;
}
