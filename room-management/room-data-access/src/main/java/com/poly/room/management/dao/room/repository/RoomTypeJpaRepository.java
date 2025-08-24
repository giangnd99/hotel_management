package com.poly.room.management.dao.room.repository;

import com.poly.room.management.dao.room.entity.RoomTypeEntity;
import com.poly.room.management.domain.entity.RoomType;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoomTypeJpaRepository extends JpaRepository<RoomTypeEntity, UUID> {

    @Query("SELECT rt FROM RoomTypeEntity rt JOIN FETCH rt.furnituresRequirements WHERE rt.roomTypeId = :id")
    Optional<RoomTypeEntity> findByIdWithFurnitures(@Param("id") UUID id);
}
