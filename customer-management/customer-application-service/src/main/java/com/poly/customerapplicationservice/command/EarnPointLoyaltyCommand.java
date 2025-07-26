package com.poly.customerapplicationservice.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EarnPointLoyaltyCommand {
    private UUID customerId;
    private BigDecimal amount;
    private String description;
}
