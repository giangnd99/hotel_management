package com.poly.room.management.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @NotBlank(message = "Description is required")
    private String description;
}
