package com.poly.room.management.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomMaintenanceResponse {
    private Integer id;
    private Integer roomId;
    private String staffId;
    private Timestamp scheduledDate;
    private Timestamp startDate;
    private Timestamp completionDate;
    private Integer maintenanceTypeId;
    private String maintenanceTypeName;
    private String description;
    private String maintenanceStatus;
}
