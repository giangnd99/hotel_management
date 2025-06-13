package com.poly.room.management.dao.room.repository;

import com.poly.room.management.dao.room.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomJpaRepository extends JpaRepository<RoomEntity, Integer> {
}
