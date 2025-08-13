package com.poly.room.management.domain.port.out.repository;

import java.util.List;
import java.util.Optional;

public interface RoomStatusRepository {
    RoomStatus save(RoomStatus roomStatus);

    Optional<RoomStatus> findById(Integer id);

    List<RoomStatus> findAll();

    RoomStatus update(RoomStatus roomStatus);

    void delete(RoomStatus roomStatus);
}
