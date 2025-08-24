package com.poly.room.management.domain.port.out.repository;

import com.poly.room.management.domain.entity.Furniture;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FurnitureRepository {

    Optional<Furniture> findById(UUID id);

    Furniture save(Furniture furniture);

    List<Furniture> findAll();

    void deleteById(UUID id);

    Furniture update(Furniture furniture);
}
