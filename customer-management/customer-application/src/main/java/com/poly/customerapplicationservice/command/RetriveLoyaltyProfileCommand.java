package com.poly.customerapplicationservice.command;

import lombok.Getter;

import java.util.UUID;

@Getter
public class RetriveLoyaltyProfileCommand {
    private UUID customerId;
}
