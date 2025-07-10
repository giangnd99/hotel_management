package com.poly.customerapplicationservice.port.input;

import com.poly.customerapplicationservice.command.RetriveLoyaltyProfileCommand;
import com.poly.customerapplicationservice.dto.LoyaltyDto;

public interface LoyaltyUsecase {
    LoyaltyDto retrieveLoyaltyProfile(RetriveLoyaltyProfileCommand command);
}
