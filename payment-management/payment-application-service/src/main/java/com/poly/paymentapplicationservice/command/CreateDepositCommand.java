package com.poly.paymentapplicationservice.command;

import com.poly.paymentdomain.model.entity.valueobject.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class CreateDepositCommand {
    private UUID bookingId;
    private String name;
    private Integer quantity;
    private BigDecimal amount;
    private PaymentMethod method;
}
