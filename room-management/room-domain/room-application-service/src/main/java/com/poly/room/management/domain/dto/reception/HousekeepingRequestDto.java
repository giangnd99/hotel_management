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
    private String guestName;
    private String requestType; // DAILY_CLEANING, TURNDOWN, DEEP_CLEANING, LINEN_CHANGE
    private String status; // REQUESTED, IN_PROGRESS, COMPLETED, CANCELLED
    private String priority; // LOW, MEDIUM, HIGH, URGENT
    private LocalDateTime requestedAt;
    private LocalDateTime scheduledAt;
    private LocalDateTime completedAt;
    private String requestedBy;
    private String assignedTo;
    private String completedBy;
    private String notes;
    private String specialInstructions;
    private Boolean isUrgent;
}