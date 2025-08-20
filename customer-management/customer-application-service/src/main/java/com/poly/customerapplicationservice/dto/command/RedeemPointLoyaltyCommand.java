package com.poly.customerapplicationservice.dto.command;

import com.poly.customerapplicationservice.shared.RedeemTargetType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class RedeemPointLoyaltyCommand {
    private UUID customerId;
    private BigDecimal amount;
    private RedeemTargetType targetType;
    private UUID targetId;
    private String description;
}
