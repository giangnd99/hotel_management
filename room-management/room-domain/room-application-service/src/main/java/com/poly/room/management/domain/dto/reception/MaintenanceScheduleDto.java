package com.poly.room.management.domain.dto.reception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceScheduleDto {
    private UUID scheduleId;
    private LocalDate scheduledDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String roomNumber;
    private String issueType;
    private String description;
    private String assignedTo;
    private String priority; // LOW, MEDIUM, HIGH, CRITICAL
    private String status; // SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED
    private BigDecimal estimatedCost;
    private String estimatedDuration;
    private List<String> requiredTools;
    private List<String> requiredParts;
    private String supervisor;
    private String notes;
    private Boolean requiresRoomEvacuation;
    private Boolean isUrgent;
}
