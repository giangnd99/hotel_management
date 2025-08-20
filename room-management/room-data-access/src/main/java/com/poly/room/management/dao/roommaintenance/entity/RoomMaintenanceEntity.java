package com.poly.room.management.dao.roommaintenance.entity;

import com.poly.room.management.dao.room.entity.MaintenanceTypeEntity;
import com.poly.room.management.dao.room.entity.RoomEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "room_maintenance")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomMaintenanceEntity {

    @Id
    private UUID id;

    @Column(name = "room_number", nullable = false, length = 10)
    private String roomNumber;
    @ManyToOne
    @JoinColumn(name = "maintenance_type_id", nullable = false)
    private MaintenanceTypeEntity maintenanceType;
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private RoomEntity room;

    @Enumerated(EnumType.STRING)
    @Column(name = "issue_type", nullable = false)
    private IssueType issueType;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private MaintenancePriority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MaintenanceStatus status;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "requested_by", length = 50)
    private String requestedBy;

    @Column(name = "assigned_to", length = 50)
    private String assignedTo;

    @Column(name = "requested_at")
    private LocalDateTime requestedAt;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "estimated_cost", precision = 10, scale = 2)
    private BigDecimal estimatedCost;

    @Column(name = "actual_cost", precision = 10, scale = 2)
    private BigDecimal actualCost;

    @Column(name = "is_urgent")
    private Boolean isUrgent;

    @Column(name = "special_instructions", columnDefinition = "TEXT")
    private String specialInstructions;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public enum IssueType {
        PLUMBING, ELECTRICAL, HVAC, STRUCTURAL, APPLIANCE, OTHER
    }

    public enum MaintenancePriority {
        LOW, MEDIUM, HIGH, URGENT, CRITICAL
    }

    public enum MaintenanceStatus {
        REQUESTED, ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED
    }
}
