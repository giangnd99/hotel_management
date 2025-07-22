package com.poly.room.management.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMaintenanceRequest {
    @NotNull(message = "Room ID is required")
    private Integer roomId;

    private String staffId; // StaffId có thể null

    @NotNull(message = "Maintenance type ID is required")
    private Integer maintenanceTypeId;

    @NotBlank(message = "Maintenance status is required")
    private String maintenanceStatus;

    @NotBlank(message = "Actual start date is required")
    private LocalDateTime actualStartDate;

    @NotBlank(message = "Completion date is required")
    private LocalDateTime completionDate;

    @NotBlank(message = "Maintenance date is required")
    private LocalDateTime maintenanceDate;

    @NotBlank(message = "Maintenance name is required")
    private String maintenanceName;

    @NotBlank(message = "Description is required")
    private String description;
}
