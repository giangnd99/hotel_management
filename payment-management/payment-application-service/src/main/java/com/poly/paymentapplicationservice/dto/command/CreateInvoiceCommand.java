package com.poly.paymentapplicationservice.dto.command;

import com.poly.paymentdomain.model.entity.value_object.Description;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
public class CreateInvoiceCommand {
    private UUID referenceId;
    private UUID customerId;
    private UUID staffId;
    private BigDecimal tax;
    private BigDecimal subTotal;
    private BigDecimal totalAmount;
    private Description note;
}
