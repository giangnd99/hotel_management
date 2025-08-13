package com.poly.paymentapplicationservice.dto.command.ok;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class AddChargeToInvoiceCommand {
    private UUID bookingId;
    private UUID serviceChargeId;
    private String nameRestaurant;
    private int quantity;
    private BigDecimal unitPrice;
}