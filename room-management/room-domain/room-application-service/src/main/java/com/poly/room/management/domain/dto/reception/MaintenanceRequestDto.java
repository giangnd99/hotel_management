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
    private String issueType; // PLUMBING, ELECTRICAL, HVAC, FURNITURE, APPLIANCE, STRUCTURAL
    private String description;
    private String status; // REQUESTED, ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED
    private String priority; // LOW, MEDIUM, HIGH, CRITICAL
    private LocalDateTime requestedAt;
    private LocalDateTime scheduledAt;
    private LocalDateTime completedAt;
    private String requestedBy;
    private String assignedTo;
    private String completedBy;
    private String notes;
    private String estimatedDuration;
    private String actualDuration;
    private BigDecimal estimatedCost;
    private BigDecimal actualCost;
    private Boolean isUrgent;
    private Boolean requiresRoomEvacuation;
}
