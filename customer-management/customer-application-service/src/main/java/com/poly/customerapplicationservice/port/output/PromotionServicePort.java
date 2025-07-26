package com.poly.customerapplicationservice.port.output;

import com.poly.customerapplicationservice.command.RedeemVoucherCommand;

public interface PromotionServicePort {
    boolean redeemVoucher(RedeemVoucherCommand command);
}