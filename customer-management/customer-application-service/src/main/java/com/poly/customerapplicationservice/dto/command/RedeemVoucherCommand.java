package com.poly.customerapplicationservice.dto.command;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RedeemVoucherCommand {
    private UUID customerId;
    private UUID voucherId;
}
