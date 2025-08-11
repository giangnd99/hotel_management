package com.poly.booking.management.domain.port.out.repository;

import com.poly.booking.management.domain.entity.Room;

import java.util.Optional;
import java.util.UUID;

public interface RoomRepository {

    Optional<Room> findById(UUID roomId);

    void save(Room room);

}
