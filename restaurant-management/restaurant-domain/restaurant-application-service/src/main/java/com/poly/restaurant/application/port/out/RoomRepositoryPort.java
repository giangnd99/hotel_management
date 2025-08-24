package com.poly.restaurant.application.port.out;

import com.poly.restaurant.domain.entity.Room;

import java.util.Optional;

public interface RoomRepositoryPort {

    Optional<Room> findById(String id);
    Room save(Room room);
    boolean existsById(String id);
    void deleteById(String id);
}
