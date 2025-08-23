package com.poly.ai.management.domain.dto;

import com.poly.domain.valueobject.PaymentMethod;
import com.poly.domain.valueobject.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    private UUID id;

    private UUID referenceId;

    private PaymentStatus status;

    private BigDecimal amount;

    private PaymentMethod method;

    private LocalDateTime paidAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private long orderCode;

    private String paymentLink;

    private String description;

}
