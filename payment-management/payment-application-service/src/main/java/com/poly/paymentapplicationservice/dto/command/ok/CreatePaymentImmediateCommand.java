package com.poly.paymentapplicationservice.dto.command.ok;

import com.poly.domain.valueobject.PaymentMethod;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class CreatePaymentImmediateCommand {
    private List<ItemCommand> items;
    private UUID staff;
    private BigDecimal taxRate;
    private PaymentMethod method;
    private String typeSerivce;
}
