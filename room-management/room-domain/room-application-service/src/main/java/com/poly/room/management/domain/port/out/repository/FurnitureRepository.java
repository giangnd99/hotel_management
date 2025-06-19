package com.poly.room.management.domain.port.out.repository;

import com.poly.room.management.domain.entity.Furniture;

import java.util.List;
import java.util.Optional;

public interface FurnitureRepository {

    Optional<Furniture> findById(Integer id);

    Furniture save(Furniture furniture);

    List<Furniture> findAll();

    void deleteById(Integer id);

    Furniture update(Furniture furniture);
}
