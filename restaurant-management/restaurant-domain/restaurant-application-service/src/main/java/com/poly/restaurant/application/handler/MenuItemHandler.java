package com.poly.restaurant.application.handler;

import com.poly.restaurant.domain.entity.MenuItem;

import java.util.List;

public interface MenuItemHandler extends GenericHandler<MenuItem, Integer>{
    List<MenuItem> searchByName(String name);
}
