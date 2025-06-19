package com.poly.inventory.dataaccess.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "inventory_items")
public class InventoryEntity {

    @Id
    private Integer itemId;

    private String itemName;
    private String category;
    private Integer quantity;
    private Double unitPrice;
    private Integer minimumQuantity;
}
