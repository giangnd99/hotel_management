package com.poly.room.management.domain.dto.reception;

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
public class RoomServiceDto {
    private UUID serviceId;
    private String roomNumber;
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
}