package com.poly.room.management.domain.port.out.repository;

import com.poly.room.management.domain.entity.Room;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomRepository {

    Optional<Room> findById(UUID id);

    Room save(Room room);

    Room update(Room room);

    void delete(Room room);

    List<Room> findAll();


}
