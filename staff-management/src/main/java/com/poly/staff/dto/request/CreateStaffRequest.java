package com.poly.staff.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateStaffRequest {
    @NotBlank(message = "User ID is required")
    private String userId;
    
    @NotNull(message = "Department ID is required")
    private Long departmentId;
    
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Invalid phone number format")
    private String phone;
    
    @NotBlank(message = "Address is required")
    private String address;
    
    private String bankName;
    private String bankAccount;
    private String avatar;
    
    @NotNull(message = "Hire date is required")
    private LocalDate hireDate;
    
    @NotNull(message = "Base salary is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Salary must be greater than 0")
    private BigDecimal baseSalary;
    
    private Set<String> permissions;
}
