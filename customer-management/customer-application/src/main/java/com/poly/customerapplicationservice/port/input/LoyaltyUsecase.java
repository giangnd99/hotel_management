package com.poly.customerapplicationservice.port.input;

import com.poly.customerapplicationservice.command.RetriveLoyaltyProfileCommand;
import com.poly.customerapplicationservice.dto.LoytaltyDto;

public interface LoyaltyUsecase {
    LoytaltyDto retriveLoyaltyProfile(RetriveLoyaltyProfileCommand command);
}
