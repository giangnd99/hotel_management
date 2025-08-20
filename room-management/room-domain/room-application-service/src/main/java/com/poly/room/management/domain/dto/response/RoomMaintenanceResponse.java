package com.poly.room.management.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomMaintenanceResponse {
    private String id;
    private String roomId;
    private String staffId;
    private Timestamp scheduledDate;
    private Timestamp startDate;
    private Timestamp completionDate;
    private Integer maintenanceTypeId;
    private String maintenanceTypeName;
    private String description;
    private String maintenanceStatus;
}
