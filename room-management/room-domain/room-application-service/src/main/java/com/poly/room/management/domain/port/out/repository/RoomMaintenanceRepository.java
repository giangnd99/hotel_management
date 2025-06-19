package com.poly.room.management.domain.port.out.repository;

import com.poly.room.management.domain.entity.RoomMaintenance;

import java.util.List;
import java.util.Optional;

public interface RoomMaintenanceRepository {

    RoomMaintenance save(RoomMaintenance roomMaintenance);

    Optional<RoomMaintenance> findById(Integer id);

    List<RoomMaintenance> findAll();

    void deleteById(Integer id);

    List<RoomMaintenance> findByRoomId(Integer roomId);

    RoomMaintenance update(RoomMaintenance roomMaintenance);
}
