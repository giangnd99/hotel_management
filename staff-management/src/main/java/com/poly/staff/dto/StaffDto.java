package com.poly.staff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffDto {
    private String staffId;
    private String userId;
    private Long departmentId;
    private String departmentName;
    private Set<String> permissions;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String bankName;
    private String bankAccount;
    private String avatar;
    private LocalDate hireDate;
    private BigDecimal baseSalary;
    private String status; // ACTIVE, INACTIVE, ON_LEAVE, TERMINATED
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
