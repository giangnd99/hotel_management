package com.poly.restaurant.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private UUID customerId;
    private UUID userId;
    private String firstName;
    private String lastName;
    private String address;
    private LocalDate dateOfBirth;
    private BigDecimal accumulatedSpending;
    private String level; // NONE, BRONZE, SILVER, GOLD, PLATINUM, DIAMOND
    private String imageUrl;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedDate;
    private String sex; // FEMALE, MALE, OTHER
    private boolean active;
}
