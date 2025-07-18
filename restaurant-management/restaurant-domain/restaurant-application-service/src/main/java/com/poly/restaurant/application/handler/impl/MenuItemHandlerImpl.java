package com.poly.restaurant.application.handler.impl;

import com.poly.restaurant.application.annotation.DomainHandler;
import com.poly.restaurant.application.handler.MenuItemHandler;
import com.poly.restaurant.application.port.out.MenuItemRepositoryPort;
import com.poly.restaurant.application.port.out.RepositoryPort;
import com.poly.restaurant.domain.entity.MenuItem;
import lombok.RequiredArgsConstructor;

import java.util.List;

@DomainHandler
@RequiredArgsConstructor
public class MenuItemHandlerImpl extends AbstractGenericHandlerImpl<MenuItem, Integer> implements MenuItemHandler {

    private final MenuItemRepositoryPort repository;

    @Override
    protected RepositoryPort<MenuItem, Integer> getRepository() {
        return repository;
    }

    @Override
    public List<MenuItem> searchByName(String name) {
        return List.of();
    }

}
