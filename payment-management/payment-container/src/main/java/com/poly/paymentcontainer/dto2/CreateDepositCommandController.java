package com.poly.paymentcontainer.dto2;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class CreateDepositCommandController {
    private UUID bookingId;
    private String name;
    private Integer quantity;
    private BigDecimal amount;
//    private PaymentMethod method;
}
