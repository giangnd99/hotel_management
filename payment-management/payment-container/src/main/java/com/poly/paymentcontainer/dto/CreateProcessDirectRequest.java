package com.poly.paymentcontainer.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
public class CreateProcessDirectRequest {
    private List<ItemRequest> items;
    private UUID staff;
    private BigDecimal taxRate;
}
