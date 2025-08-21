package com.poly.room.management.dao.roomservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "room_services")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomServiceEntity {
    
    @Id
    @Column(columnDefinition = "uuid",updatable = false)
    private UUID serviceId;
    
    @Column(name = "room_number", nullable = false)
    private String roomNumber;
    
    @Column(name = "guest_id", columnDefinition = "uuid",updatable = false)
    private UUID guestId;
    
    @Column(name = "guest_name")
    private String guestName;
    
    @Column(name = "service_type", nullable = false)
    private String serviceType;
    
    @Column(name = "service_name", nullable = false)
    private String serviceName;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private Integer quantity;
    
    @Column(name = "unit_price")
    private BigDecimal unitPrice;
    
    @Column(name = "total_price")
    private BigDecimal totalPrice;
    
    @Enumerated(EnumType.STRING)
    private ServiceStatus status;
    
    @Column(name = "requested_at")
    private LocalDateTime requestedAt;
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    @Column(name = "requested_by")
    private String requestedBy;
    
    @Column(name = "completed_by")
    private String completedBy;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "special_instructions", columnDefinition = "TEXT")
    private String specialInstructions;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public enum ServiceStatus {
        REQUESTED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }
}
