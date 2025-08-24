package com.poly.restaurant.dataaccess.adapter;

import com.poly.restaurant.application.port.out.MenuItemRepositoryPort;
import com.poly.restaurant.dataaccess.entity.MenuItemJpaEntity;
import com.poly.restaurant.dataaccess.jpa.JpaMenuItemRepository;
import com.poly.restaurant.dataaccess.mapper.MenuItemEntityMapper;
import com.poly.restaurant.domain.entity.MenuItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MenuItemRepositoryAdapter implements MenuItemRepositoryPort {

    private final JpaMenuItemRepository jpaMenuItemRepository;

    @Override
    public MenuItem save(MenuItem item) {
        MenuItemJpaEntity entity = MenuItemEntityMapper.toEntity(item);
        return MenuItemEntityMapper.toDomain(jpaMenuItemRepository.save(entity));
    }

    @Override
    public Optional<MenuItem> findById(Integer id) {
        return jpaMenuItemRepository.findById(id)
                .map(MenuItemEntityMapper::toDomain);
    }

    @Override
    public List<MenuItem> findAll() {
        return jpaMenuItemRepository.findAll().stream()
                .map(MenuItemEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Integer id) {
        return jpaMenuItemRepository.existsById(id);
    }

    @Override
    public void deleteById(Integer id) {
        jpaMenuItemRepository.deleteById(id);
    }

    @Override
    public List<MenuItem> searchByName(String name) {
        // TODO: Implement search (tùy vào jpa repo)
        return List.of();
    }
}
