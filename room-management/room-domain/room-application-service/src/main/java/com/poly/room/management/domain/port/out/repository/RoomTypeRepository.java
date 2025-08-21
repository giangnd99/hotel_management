package com.poly.room.management.domain.port.out.repository;

import com.poly.room.management.domain.entity.RoomType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomTypeRepository {

    RoomType save(RoomType roomType);

    Optional<RoomType> findById(UUID id);

    List<RoomType> findAll();

    void deleteById(UUID id);

    RoomType update(RoomType roomType, UUID id);
}
