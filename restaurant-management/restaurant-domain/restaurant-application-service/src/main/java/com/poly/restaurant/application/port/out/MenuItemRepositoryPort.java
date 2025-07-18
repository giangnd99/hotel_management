package com.poly.restaurant.application.port.out;

import com.poly.restaurant.domain.entity.MenuItem;

import java.util.List;

public interface MenuItemRepositoryPort extends RepositoryPort<MenuItem, Integer> {
    List<MenuItem> searchByName(String name);
}
