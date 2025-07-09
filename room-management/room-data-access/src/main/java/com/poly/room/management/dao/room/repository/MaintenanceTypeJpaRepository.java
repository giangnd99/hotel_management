package com.poly.room.management.dao.room.repository;

import com.poly.room.management.dao.room.entity.MaintenanceTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintenanceTypeJpaRepository extends JpaRepository<MaintenanceTypeEntity, Integer> {
}
