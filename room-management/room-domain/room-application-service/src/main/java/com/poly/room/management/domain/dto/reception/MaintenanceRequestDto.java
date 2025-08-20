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
public class MaintenanceRequestDto {
    private UUID requestId;
    private String roomNumber;
    private String issueType; // PLUMBING, ELECTRICAL, HVAC, STRUCTURAL, APPLIANCE, OTHER
    private String priority; // LOW, MEDIUM, HIGH, URGENT, CRITICAL
    private String status; // REQUESTED, ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED
    private String description;
    private String notes;
    private String requestedBy;
    private String assignedTo;
    private LocalDateTime requestedAt;
    private LocalDateTime scheduledAt;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private BigDecimal estimatedCost;
    private BigDecimal actualCost;
    private Boolean isUrgent;
    private String specialInstructions;
}
