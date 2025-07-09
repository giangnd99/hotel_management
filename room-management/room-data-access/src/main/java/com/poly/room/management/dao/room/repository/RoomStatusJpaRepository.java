package com.poly.room.management.dao.room.repository;

import com.poly.room.management.dao.room.entity.RoomStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomStatusJpaRepository extends JpaRepository<RoomStatusEntity, Integer> {
}
