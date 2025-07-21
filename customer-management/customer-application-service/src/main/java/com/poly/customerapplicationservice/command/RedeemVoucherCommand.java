package com.poly.customerapplicationservice.command;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RedeemVoucherCommand {
    private UUID customerId;
    private UUID voucherId;
}
