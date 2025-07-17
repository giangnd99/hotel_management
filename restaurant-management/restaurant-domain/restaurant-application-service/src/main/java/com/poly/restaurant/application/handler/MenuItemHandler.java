package com.poly.restaurant.application.handler;

import com.poly.restaurant.application.dto.MenuDTO;

import java.util.List;

public interface MenuItemHandler {
    List<MenuDTO> searchByName(String name);
}
