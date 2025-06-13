package com.poly.room.management.dao.room.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MaintenanceTypeEntity {

    @Id
    private Integer maintenanceTypeId;

    private String name;
}
