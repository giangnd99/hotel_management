package com.poly.paymentapplicationservice.dto.command;

import com.poly.paymentapplicationservice.share.ItemData;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class CreatePaymentDepositCommand {
    private UUID referenceId;
    private BigDecimal amount;
    private List<ItemData> items;
    private String description;
    private String method;
}
