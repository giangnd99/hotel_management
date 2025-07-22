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
                .category(item.getCategory())
                .quantity(item.getQuantity())
                .build();
    }

    public static MenuItem toDomain(MenuItemJpaEntity entity) {
        if (entity == null) return null;
        return new MenuItem(
                new MenuItemId(entity.getId()),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getCategory(),
                entity.getQuantity()
        );
    }
}