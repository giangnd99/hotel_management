package com.poly.room.management.domain.port.out.repository;

import com.poly.room.management.domain.entity.RoomMaintenance;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomMaintenanceRepository {

    RoomMaintenance save(RoomMaintenance roomMaintenance);

    Optional<RoomMaintenance> findById(Integer id);

    List<RoomMaintenance> findAll();

    void deleteById(Integer id);

    List<RoomMaintenance> findByRoomId(UUID roomId);

    RoomMaintenance update(RoomMaintenance roomMaintenance);
}
