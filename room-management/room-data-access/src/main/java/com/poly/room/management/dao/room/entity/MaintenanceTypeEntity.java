package com.poly.room.management.dao.room.entity;

import com.poly.room.management.dao.roommaintenance.entity.RoomMaintenanceEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "maintenance_types")
public class MaintenanceTypeEntity {

    @Id
    @Column(columnDefinition = "uuid",updatable = false)
    private UUID maintenanceTypeId;

    private String name;

    @OneToMany(mappedBy = "maintenanceType", cascade = jakarta.persistence.CascadeType.ALL)
    private List<RoomMaintenanceEntity> roomMaintenances;
}
