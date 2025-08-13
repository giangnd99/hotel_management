package com.poly.paymentapplicationservice.dto.command;

import com.poly.paymentapplicationservice.share.ItemData;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
public class CreatePaymentLinkCommand {
    private long orderCode;
    private BigDecimal amount;
    private List<ItemData> items;
    private String description;
    private String method;
}
