package com.poly.room.management.dao.room.entity;

import com.poly.room.management.dao.roommaintenance.entity.RoomMaintenanceEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MaintenanceTypeEntity {

    @Id
    private Integer maintenanceTypeId;

    private String name;

    @OneToMany(mappedBy = "maintenanceType", cascade = jakarta.persistence.CascadeType.ALL)
    private List<RoomMaintenanceEntity> roomMaintenances;
}
