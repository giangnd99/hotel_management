package com.poly.ai.management.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomCleaningResponse {
    private String roomNumber;
    private String cleaningType;
    private String priority;
    private String description;
    private String status;
    private String requestedBy;
    private String assignedTo;
    private String scheduledDate;
    private String startedAt;
    private String completedAt;
    private String notes;
    private String cleaningProducts;
    private String specialInstructions;
}
