package com.poly.room.management.dao.room.repository;

import com.poly.room.management.dao.room.entity.RoomTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoomTypeJpaRepository extends JpaRepository<RoomTypeEntity, UUID> {
}
