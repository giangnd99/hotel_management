package com.poly.room.management.dao.room.repository;

import com.poly.room.management.dao.room.entity.RoomMaintenanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoomMaintenanceJpaRepository extends JpaRepository<RoomMaintenanceEntity, Integer> {
    List<RoomMaintenanceEntity> findAllByRoom_RoomId(UUID roomId);
}
