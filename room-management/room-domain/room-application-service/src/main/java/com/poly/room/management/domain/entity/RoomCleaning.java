package com.poly.room.management.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomCleaning {
    
    private UUID id;
    private UUID roomId;
    private String roomNumber;
    private String cleaningType; // DAILY, DEEP_CLEANING, POST_CHECKOUT, MAINTENANCE
    private String priority; // LOW, MEDIUM, HIGH, URGENT
    private String status; // REQUESTED, ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED
    private String description;
    private String notes;
    private String requestedBy;
    private String assignedTo;
    private LocalDateTime requestedAt;
    private LocalDateTime scheduledAt;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private Boolean isUrgent;
    private String specialInstructions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Business methods
    public boolean isRequested() {
        return "REQUESTED".equals(status);
    }
    
    public boolean isAssigned() {
        return "ASSIGNED".equals(status);
    }
    
    public boolean isInProgress() {
        return "IN_PROGRESS".equals(status);
    }
    
    public boolean isCompleted() {
        return "COMPLETED".equals(status);
    }
    
    public boolean isCancelled() {
        return "CANCELLED".equals(status);
    }
    
    public boolean canStart() {
        return isAssigned();
    }
    
    public boolean canComplete() {
        return isInProgress();
    }
    
    public boolean canCancel() {
        return isRequested() || isAssigned() || isInProgress();
    }
    
    public void assign(String staffId) {
        if (isRequested()) {
            this.status = "ASSIGNED";
            this.assignedTo = staffId;
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void start() {
        if (canStart()) {
            this.status = "IN_PROGRESS";
            this.startedAt = LocalDateTime.now();
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void complete() {
        if (canComplete()) {
            this.status = "COMPLETED";
            this.completedAt = LocalDateTime.now();
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void cancel(String reason) {
        if (canCancel()) {
            this.status = "CANCELLED";
            this.notes = reason;
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public boolean isOverdue(LocalDateTime currentDate) {
        return scheduledAt != null && currentDate.isAfter(scheduledAt) && !isCompleted();
    }
}
