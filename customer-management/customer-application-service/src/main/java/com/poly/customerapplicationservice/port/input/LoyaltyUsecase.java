package com.poly.customerapplicationservice.port.input;

import com.poly.customerapplicationservice.command.EarnPointLoyaltyCommand;
import com.poly.customerapplicationservice.command.RetrieveLoyaltyProfileCommand;
import com.poly.customerapplicationservice.command.RetrieveLoyaltyTransactionCommand;
import com.poly.customerapplicationservice.command.RedeemPointLoyaltyCommand;
import com.poly.customerapplicationservice.dto.LoyaltyPointDto;
import com.poly.customerapplicationservice.dto.LoyaltyTransactionDto;

import java.util.List;

public interface LoyaltyUsecase {
    LoyaltyPointDto earnPoint(EarnPointLoyaltyCommand command);
    LoyaltyPointDto redeemPoint(RedeemPointLoyaltyCommand command);
    LoyaltyPointDto retrieveLoyaltyProfile(RetrieveLoyaltyProfileCommand command);
    List<LoyaltyTransactionDto> viewTransactionHistory(RetrieveLoyaltyTransactionCommand command);
}
