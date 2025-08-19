package com.poly.room.management.domain.port.out.repository;

import com.poly.room.management.domain.entity.MaintenanceType;

import java.util.List;
import java.util.Optional;

public interface MaintenanceTypeRepository {

    MaintenanceType save(MaintenanceType maintenanceType);

    Optional<MaintenanceType> findById(Integer id);

    MaintenanceType update(MaintenanceType maintenanceType);

    void delete(MaintenanceType maintenanceType);

    List<MaintenanceType> findAll();
    
    Optional<MaintenanceType> findByName(String name);
}
