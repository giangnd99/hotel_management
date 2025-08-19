package com.poly.room.management.dao.room.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomMaintenanceEntity {

    @Id
    private UUID maintenanceId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    private RoomEntity room;

    private String assignedTo;

    private Timestamp scheduledDate;
    private Timestamp completedDate;
    private Timestamp maintenanceDate;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "maintenance_type_id")
    private MaintenanceTypeEntity maintenanceType;

    private String description;

    private String priority;

    private String status;
}
