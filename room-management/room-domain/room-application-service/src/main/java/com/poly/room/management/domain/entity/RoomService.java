package com.poly.room.management.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomService {
    
    private UUID serviceId;
    private String roomNumber;
    private UUID guestId;
    private String guestName;
    private String serviceType; // FOOD, CLEANING, MAINTENANCE, AMENITIES
    private String serviceName;
    private String description;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private String status; // REQUESTED, IN_PROGRESS, COMPLETED, CANCELLED
    private LocalDateTime requestedAt;
    private LocalDateTime completedAt;
    private String requestedBy;
    private String completedBy;
    private String notes;
    private String specialInstructions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Business methods
    public boolean isRequested() {
        return "REQUESTED".equals(status);
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
        return isRequested();
    }
    
    public boolean canComplete() {
        return isInProgress();
    }
    
    public boolean canCancel() {
        return isRequested() || isInProgress();
    }
    
    public void startService() {
        if (canStart()) {
            this.status = "IN_PROGRESS";
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void completeService() {
        if (canComplete()) {
            this.status = "COMPLETED";
            this.completedAt = LocalDateTime.now();
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void cancelService() {
        if (canCancel()) {
            this.status = "CANCELLED";
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void calculateTotalPrice() {
        if (quantity != null && unitPrice != null) {
            this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
}
