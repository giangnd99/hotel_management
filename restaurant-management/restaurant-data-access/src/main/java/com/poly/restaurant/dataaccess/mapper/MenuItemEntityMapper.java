package com.poly.restaurant.dataaccess.mapper;

import com.poly.restaurant.dataaccess.entity.MenuItemJpaEntity;
import com.poly.restaurant.domain.entity.MenuItem;
import com.poly.restaurant.domain.value_object.MenuItemId;

public class MenuItemEntityMapper {

    public static MenuItemJpaEntity toJpa(MenuItem item) {
        if (item == null) return null;
        MenuItemJpaEntity entity = new MenuItemJpaEntity();
        entity.setId(item.getId().getValue());
        entity.setName(item.getName());
        entity.setDescription(item.getDescription());
        entity.setPrice(item.getPrice());
        entity.setCategory(item.getCategory());
        entity.setQuantity(item.getQuantity());
        return entity;
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
