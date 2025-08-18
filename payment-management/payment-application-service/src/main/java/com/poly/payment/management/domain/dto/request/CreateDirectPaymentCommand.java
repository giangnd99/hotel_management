package com.poly.payment.management.domain.dto.request;

import com.poly.payment.management.domain.dto.ItemData;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class CreateDirectPaymentCommand {
    private @NonNull List<ItemData> items;
    private UUID referenceId;
    private UUID staffId;
    private UUID customerId;
    private BigDecimal voucherAmount;
    private BigDecimal subTotal;
    private BigDecimal taxRate;
    private String notes;
}
