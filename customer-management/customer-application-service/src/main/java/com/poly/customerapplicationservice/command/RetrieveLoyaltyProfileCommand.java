package com.poly.customerapplicationservice.command;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class RetrieveLoyaltyProfileCommand {
    private UUID customerId;
}
