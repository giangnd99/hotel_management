package com.poly.paymentapplicationservice.command;

import lombok.Getter;

import java.util.UUID;

@Getter
public class RetrieveInvoiceCommand {
    private UUID customerId;
    private int page;
    private int size;
}
