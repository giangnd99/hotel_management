package com.poly.staff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffDto {
    private String staffId;
    private String name;
    private String email;
    private String phone;
    private String department;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
