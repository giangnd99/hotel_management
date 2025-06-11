package com.poly.inventory.dataaccess.entity;

import com.poly.inventory.domain.value_object.ItemId;
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
    @AttributeOverride(name = "value", column = @Column(name = "item_id"))
    private ItemId itemId;

    private String itemName;
    private String category;
    private Integer quantity;
    private Double unitPrice;
    private Integer minimumQuantity;
}
