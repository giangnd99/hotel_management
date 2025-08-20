package com.poly.customerapplicationservice.dto.command;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RetrieveCustomerProfileCommand {
    private UUID userId;
}
