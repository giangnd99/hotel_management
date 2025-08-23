package com.poly.customerapplicationservice.dto.command;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class RetrieveLoyaltyProfileCommand {
    private UUID customerId;
}
