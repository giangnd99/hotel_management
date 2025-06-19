package com.poly.room.management.domain.port.out.repository;

import com.poly.room.management.domain.entity.Furniture;

import java.util.List;

public interface FurnitureRepository {

    Furniture findById(Integer id);

    Furniture save(Furniture furniture);

    List<Furniture> findAll();

    void deleteById(Integer id);

    Furniture update(Furniture furniture);
}
