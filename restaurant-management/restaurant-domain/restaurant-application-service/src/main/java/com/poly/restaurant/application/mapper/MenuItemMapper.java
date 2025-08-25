package com.poly.restaurant.application.mapper;

import com.poly.restaurant.application.dto.MenuDTO;
import com.poly.restaurant.domain.entity.MenuItem;
import com.poly.restaurant.domain.value_object.MenuItemId;

public class MenuItemMapper {

    public static MenuItem toEntity(MenuDTO dto) {
        if (dto == null) return null;

        MenuItemId id = dto.id() != null ? new MenuItemId(dto.id()) : null;

        return new MenuItem(
                id,
                dto.name(),
                dto.description(),
                dto.price(),
                dto.categoryId());
    }

    public static MenuDTO toDto(MenuItem entity) {
        if (entity == null) return null;

        return new MenuDTO(
                entity.getId().getValue(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice().abs(),
                entity.getCategoryId(),
                entity.getStatus().name()
        );
    }
}
