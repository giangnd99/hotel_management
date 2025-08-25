package com.poly.room.management.domain.dto.reception;

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
public class HousekeepingRequestDto {
    private UUID requestId;
    private String roomNumber;
    private String requestType; // DAILY, DEEP_CLEANING, POST_CHECKOUT, MAINTENANCE
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
}