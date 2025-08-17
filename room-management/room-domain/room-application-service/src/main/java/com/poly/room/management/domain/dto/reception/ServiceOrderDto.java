package com.poly.room.management.domain.dto.reception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOrderDto {
    private UUID orderId;
    private String roomNumber;
    private String guestName;
    private String guestPhone;
    private List<ServiceOrderItemDto> items;
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal totalAmount;
    private String status; // PENDING, CONFIRMED, PREPARING, DELIVERED, CANCELLED
    private LocalDateTime orderTime;
    private LocalDateTime expectedDeliveryTime;
    private LocalDateTime actualDeliveryTime;
    private String specialInstructions;
    private String paymentMethod;
    private String paymentStatus;
    private String orderedBy;
    private String deliveredBy;
    private String notes;
}
