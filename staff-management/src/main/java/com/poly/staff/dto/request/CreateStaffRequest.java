package com.poly.staff.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateStaffRequest {
    
    @NotBlank(message = "Staff ID is required")
    @Pattern(regexp = "^[A-Z0-9]{3,10}$", message = "Staff ID must be 3-10 characters long and contain only uppercase letters and numbers")
    private String staffId;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
    
    @Pattern(regexp = "^[0-9+\\-()\\s]{10,15}$", message = "Phone number must be 10-15 digits and can contain +, -, (), and spaces")
    private String phone;
    
    private String department;
}
