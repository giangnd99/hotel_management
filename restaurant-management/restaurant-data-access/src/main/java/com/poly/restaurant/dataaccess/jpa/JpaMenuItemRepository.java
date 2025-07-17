package com.poly.restaurant.dataaccess.jpa;

import com.poly.restaurant.dataaccess.entity.MenuItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMenuItemRepository extends JpaRepository<MenuItemJpaEntity, Integer> {
}
