package com.poly.room.management.dao.room.repository;

import com.poly.room.management.dao.room.entity.RoomTypeFurnitureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoomTypeFurnitureJpaRepository extends JpaRepository<RoomTypeFurnitureEntity, UUID> {

    List<RoomTypeFurnitureEntity> findAllByRoomType_RoomTypeId(UUID roomTypeId);
}
