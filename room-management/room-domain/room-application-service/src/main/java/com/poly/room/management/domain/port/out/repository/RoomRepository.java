package com.poly.room.management.domain.port.out.repository;

import com.poly.room.management.domain.entity.Room;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {

    Optional<Room> findById(Integer id);

    Room save(Room room);

    Room update(Room room);

    void delete(Room room);

    List<Room> findAll();
}
