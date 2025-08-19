package com.poly.booking.management.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class CreateDepositRequest {
    private UUID referenceId;
    private BigDecimal amount;
    private List<ItemRequest> items;
    private String description;
}
