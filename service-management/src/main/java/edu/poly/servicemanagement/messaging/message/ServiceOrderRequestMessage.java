package edu.poly.servicemanagement.messaging.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOrderRequestMessage {
    private String orderId;
    private String orderNumber;
    private Integer serviceId;
    private String customerId;
    private String roomId;
    private Integer quantity;
    private BigDecimal totalAmount;
    private String status;
    private String paymentStatus;
    private String specialInstructions;
    private LocalDateTime orderDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
