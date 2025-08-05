package com.poly.paymentapplicationservice.command;

import com.poly.paymentapplicationservice.share.ItemData;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class CreateDepositPaymentLinkConmand {
    private long referenceCode;
    private BigDecimal amount;
    private String description;
    private List<ItemData> items;
}
