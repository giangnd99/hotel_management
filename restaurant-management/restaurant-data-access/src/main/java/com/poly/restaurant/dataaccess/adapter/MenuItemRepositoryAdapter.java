package com.poly.restaurant.dataaccess.adapter;

import com.poly.restaurant.application.port.out.MenuItemRepositoryPort;
import com.poly.restaurant.dataaccess.entity.MenuItemJpaEntity;
import com.poly.restaurant.dataaccess.mapper.MenuItemEntityMapper;
import com.poly.restaurant.dataaccess.repository.JpaMenuItemRepository;
import com.poly.restaurant.domain.entity.MenuItem;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MenuItemRepositoryAdapter implements MenuItemRepositoryPort {

    private final JpaMenuItemRepository jpaRepository;

    public MenuItemRepositoryAdapter(JpaMenuItemRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public MenuItem save(MenuItem item) {
        MenuItemJpaEntity entity = MenuItemEntityMapper.toJpa(item);
        MenuItemJpaEntity saved = jpaRepository.save(entity);
        return MenuItemEntityMapper.toDomain(saved);
    }

    @Override
    public Optional<MenuItem> findById(Integer id) {
        return jpaRepository.findById(id)
                .map(MenuItemEntityMapper::toDomain);
    }

    @Override
    public List<MenuItem> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(MenuItemEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Integer id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public void deleteById(Integer id) {
        jpaRepository.deleteById(id);
    }
}
