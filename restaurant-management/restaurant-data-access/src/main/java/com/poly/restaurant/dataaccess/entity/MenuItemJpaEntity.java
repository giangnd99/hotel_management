package com.poly.restaurant.dataaccess.entity;

import com.poly.restaurant.domain.entity.MenuItemStatus;
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
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MenuItemStatus status;
}
