package com.poly.ai.management.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class BookingDto {
    private UUID bookingId;
    private UUID customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private UUID roomId;
    private String roomNumber;
    private String roomType;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer numberOfGuests;
    private BigDecimal totalAmount;
    private String status;
    private String paymentStatus; 
    private String specialRequests;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String notes;
}
