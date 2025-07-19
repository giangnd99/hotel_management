package com.poly.restaurant.dataaccess.jpa;

import com.poly.restaurant.dataaccess.entity.MenuItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaMenuItemRepository extends JpaRepository<MenuItemJpaEntity, Integer> {
    List<MenuItemJpaEntity> findByNameContainingIgnoreCase(String name);
}
