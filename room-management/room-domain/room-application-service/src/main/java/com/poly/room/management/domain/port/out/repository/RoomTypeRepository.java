package com.poly.room.management.domain.port.out.repository;

import com.poly.room.management.domain.entity.RoomType;

import java.util.List;
import java.util.Optional;

public interface RoomTypeRepository {

    RoomType save(RoomType roomType);

    Optional<RoomType> findById(Integer id);

    List<RoomType> findAll();

    void deleteById(Integer id);

    RoomType update(RoomType roomType, Integer id);
}
