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
                .id(String.valueOf(item.getId().getValue()))
                .name(item.getName())
                .description(item.getDescription())
                .price(item.getPrice())
                .categoryId(null)
                .isAvailable(item.isAvailable())
                .imageUrl(null)
                .preparationTime(null)
                .createdAt(null)
                .updatedAt(null)
                .build();
    }

    public static MenuItem toDomain(MenuItemJpaEntity entity) {
        if (entity == null) return null;
        int mappedId;
        try {
            mappedId = Integer.parseInt(entity.getId());
        } catch (NumberFormatException ex) {
            mappedId = Math.abs(entity.getId().hashCode());
        }
        return new MenuItem(
                new MenuItemId(mappedId),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getCategoryId(),
                0
        );
    }
}