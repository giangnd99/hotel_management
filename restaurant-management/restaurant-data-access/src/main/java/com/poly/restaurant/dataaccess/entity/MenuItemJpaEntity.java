package com.poly.restaurant.dataaccess.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "menu_items")
public class MenuItemJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private int quantity;
}
