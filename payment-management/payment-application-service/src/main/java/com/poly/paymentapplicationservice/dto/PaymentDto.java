package com.poly.paymentapplicationservice.dto;

import com.poly.paymentdomain.model.entity.valueobject2.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class PaymentDto {
    private UUID staffId;
    private PaymentStatus paymentStatus;
    private BigDecimal amount;
    private PaymentMethod method;
    private LocalDateTime paidAt;
    private String referenceCode;
}
