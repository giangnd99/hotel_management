package com.poly.paymentapplicationservice.dto;

import com.poly.paymentdomain.model.entity.value_object.InvoiceStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class InvoiceDto {
    private UUID id;
    private UUID bookingId;
    private UUID customerId;
    private UUID staffIdCreated;
    private UUID staffIdUpdated;
    private UUID voucherId;
    private BigDecimal subTotal;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private InvoiceStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
    List<InvoiceItemDto> items;
    private String note;
}
