package com.poly.staff.model;

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
public class StaffModel {
    private String staffId;
    private UUID userId;
    private String name;
    private String email;
    private String phone;
    private String department;
    private String status; // ACTIVE, INACTIVE, ON_LEAVE, TERMINATED
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
