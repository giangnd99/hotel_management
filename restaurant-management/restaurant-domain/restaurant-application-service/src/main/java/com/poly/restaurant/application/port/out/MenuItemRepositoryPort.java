package com.poly.restaurant.application.port.out;

import com.poly.restaurant.application.annotation.DomainHandler;
import com.poly.restaurant.domain.entity.MenuItem;

import java.util.List;
import java.util.Optional;

@DomainHandler
public interface MenuItemRepositoryPort extends RepositoryPort<MenuItem, Integer> {
    MenuItem save(MenuItem item);

    Optional<MenuItem> findById(Integer id);

    List<MenuItem> findAll();

    boolean existsById(Integer id);

    void deleteById(Integer id);
}