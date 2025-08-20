package com.poly.staff.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStaffRequest {
    
    private String name;
    
    @Email(message = "Email must be valid")
    private String email;
    
    @Pattern(regexp = "^[0-9+\\-()\\s]{10,15}$", message = "Phone number must be 10-15 digits and can contain +, -, (), and spaces")
    private String phone;
    
    private String department;
    
    private String status; // ACTIVE, INACTIVE, ON_LEAVE, TERMINATED
}
