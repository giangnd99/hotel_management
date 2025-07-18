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
@Table(name = "menu_items")
public class MenuItemJpaEntity {
    @Id
    @Column(name = "menu_item_id")
    private Integer id;
    @Column(name = "item_name")
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private Integer quantity;
}
