package com.poly.paymentcontainer.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class CreateDepositRequest {
    private UUID bookingId;
    private String name;
    private Integer quantity;
    private BigDecimal amount;
    private String method;
}
