package com.poly.customerapplicationservice.dto;

import com.poly.customerapplicationservice.shared.Level;
import com.poly.customerdomain.model.entity.Customer;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class CustomerDto {
    private UUID userId;
    private String firstName;
    private String lastName;
    private String address;
    private LocalDate dateOfBirth;
    private BigDecimal accumulatedSpending;
    private Level level;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public static CustomerDto from(Customer customer) {
        CustomerDto dto = new CustomerDto();
        dto.setUserId(customer.getUserId());
        dto.setFirstName(customer.getFullName().getFirstName());
        dto.setLastName(customer.getFullName().getLastName());
        dto.setDateOfBirth(customer.getDateOfBirth().getValue());
        dto.setAccumulatedSpending(customer.getAccumulatedSpending().getAmount());
        dto.setLevel(Level.fromDomain(customer.getLevel()));
        dto.setAddress(customer.getAddress().toFullAddress());
        dto.setCreatedDate(customer.getCreatedAt());
        dto.setUpdatedDate(customer.getUpdatedAt());
        return dto;
    }
}