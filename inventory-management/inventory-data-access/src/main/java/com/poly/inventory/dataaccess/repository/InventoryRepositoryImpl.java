package com.poly.inventory.dataaccess.repository;

import com.poly.inventory.dataaccess.jpa.InventoryJpaRepository;
import com.poly.inventory.dataaccess.mapper.InventoryEntityMapper;
import com.poly.inventory.domain.InventoryRepository;
import com.poly.inventory.domain.entity.InventoryItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryRepositoryImpl implements InventoryRepository {
    private final InventoryJpaRepository jpaRepository;

    public InventoryRepositoryImpl(InventoryJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public InventoryItem save(InventoryItem item) {
        return InventoryEntityMapper.toDomain(
                jpaRepository.save(InventoryEntityMapper.toEntity(item))
        );
    }

    @Override
    public Optional<InventoryItem> findById(Integer id) {
        return jpaRepository.findById(id).map(InventoryEntityMapper::toDomain);
    }

    @Override
    public List<InventoryItem> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(InventoryEntityMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Integer id) {
        jpaRepository.deleteById(id);
    }
}
