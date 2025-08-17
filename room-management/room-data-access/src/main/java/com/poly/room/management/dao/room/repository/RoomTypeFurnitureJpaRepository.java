package com.poly.room.management.dao.room.repository;

import com.poly.room.management.dao.room.entity.RoomTypeFurnitureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomTypeFurnitureJpaRepository extends JpaRepository<RoomTypeFurnitureEntity, Integer> {

    List<RoomTypeFurnitureEntity> findAllByRoomType_RoomTypeId(Integer roomTypeId);
}
