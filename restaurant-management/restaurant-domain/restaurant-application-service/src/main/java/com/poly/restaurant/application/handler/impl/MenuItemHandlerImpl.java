package com.poly.restaurant.application.handler.impl;

import com.poly.restaurant.application.annotation.DomainHandler;
import com.poly.restaurant.application.port.out.RepositoryPort;
import com.poly.restaurant.domain.entity.MenuItem;

@DomainHandler
public class MenuItemHandlerImpl extends AbstractGenericHandlerImpl<MenuItem, Integer> {

    private final RepositoryPort<MenuItem, Integer> menuItemRepository;

    public MenuItemHandlerImpl(RepositoryPort<MenuItem, Integer> menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    protected RepositoryPort<MenuItem, Integer> getRepository() {
        return menuItemRepository;
    }
}
