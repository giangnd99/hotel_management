package edu.poly.servicemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateServiceOrderRequest {
    @NotNull(message = "Service ID is required")
    private Integer serviceId;
    
    @NotBlank(message = "Customer ID is required")
    private String customerId;
    
    private String roomId;
    
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity = 1;
    
    private String specialInstructions;
}
