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
    private String roomId;

    private String staffId; // StaffId có thể null

    private Integer maintenanceTypeId;

    private String maintenanceStatus;

    private LocalDateTime actualStartDate;

    private LocalDateTime completionDate;

    private LocalDateTime maintenanceDate;

    private String maintenanceName;

    private String description;
}
