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
    @Column(name = "id")
    private String id;
    
    @Column(name = "name")
    private String name;
    
    private String description;
    private BigDecimal price;
    
    @Column(name = "category_id")
    private String categoryId;
    
    @Column(name = "is_available")
    private Boolean isAvailable;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(name = "preparation_time")
    private Integer preparationTime;
    
    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;
}
