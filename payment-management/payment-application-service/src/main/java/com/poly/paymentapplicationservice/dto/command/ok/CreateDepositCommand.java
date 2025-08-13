package com.poly.paymentapplicationservice.dto.command.ok;

import com.poly.domain.valueobject.PaymentMethod;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
public class CreateDepositCommand {
    private UUID bookingId;
    private String name;
    private Integer quantity;
    private BigDecimal amount;
    private PaymentMethod method;
}
