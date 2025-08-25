package com.poly.restaurant.dataaccess.mapper;

import com.poly.restaurant.dataaccess.entity.MenuItemJpaEntity;
import com.poly.restaurant.domain.entity.MenuItem;
import com.poly.restaurant.domain.value_object.MenuItemId;

public class MenuItemEntityMapper {

    private MenuItemEntityMapper() {
        // private constructor to prevent instantiation
    }

    public static MenuItemJpaEntity toEntity(MenuItem item) {
        if (item == null) return null;

        return MenuItemJpaEntity.builder()
                .id(item.getId().getValue())
                .name(item.getName())
                .description(item.getDescription())
                .price(item.getPrice())
                .categoryId(item.getCategoryId())
                .isAvailable(item.isAvailable())
                .imageUrl(null)
                .preparationTime(null)
                .createdAt(null)
                .updatedAt(null)
                .build();
    }

    public static MenuItem toDomain(MenuItemJpaEntity entity) {
        if (entity == null) return null;
        
        return new MenuItem(
                new MenuItemId(entity.getId()),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getCategoryId()
        );
    }
}