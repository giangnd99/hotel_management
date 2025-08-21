package com.poly.room.management.dao.roomcleaning.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "room_cleaning")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomCleaningEntity {

    @Id
    @Column(columnDefinition = "uuid",updatable = false)
    private UUID id;

    @Column(name = "room_id", nullable = false, columnDefinition = "uuid",updatable = false)
    private UUID roomId;

    @Column(name = "room_number", nullable = false, length = 10)
    private String roomNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "cleaning_type", nullable = false)
    private CleaningType cleaningType;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private CleaningPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CleaningStatus status;

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

    @Column(name = "is_urgent")
    private Boolean isUrgent;

    @Column(name = "special_instructions", columnDefinition = "TEXT")
    private String specialInstructions;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public enum CleaningType {
        DAILY, DEEP_CLEANING, POST_CHECKOUT, MAINTENANCE
    }

    public enum CleaningPriority {
        LOW, MEDIUM, HIGH, URGENT
    }

    public enum CleaningStatus {
        REQUESTED, ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED
    }
}
