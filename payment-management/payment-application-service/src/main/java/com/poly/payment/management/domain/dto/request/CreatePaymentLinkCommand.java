package com.poly.payment.management.domain.dto.request;

import com.poly.payment.management.domain.dto.ItemData;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
@Setter
public class CreatePaymentLinkCommand {
    private long orderCode;
    private BigDecimal amount;
    private List<ItemData> items;
    private String description;
    private String method;
}
