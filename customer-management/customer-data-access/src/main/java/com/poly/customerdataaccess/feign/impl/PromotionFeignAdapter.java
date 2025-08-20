package com.poly.customerdataaccess.feign.impl;

import com.poly.customerapplicationservice.dto.command.RedeemVoucherCommand;
import com.poly.customerapplicationservice.port.output.PromotionServicePort;
import com.poly.customerdataaccess.feign.PromotionFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PromotionFeignAdapter implements PromotionServicePort {

    private final PromotionFeignClient promotionFeignClient;

    @Override
    public boolean redeemVoucher(RedeemVoucherCommand command) {
//        return promotionFeignClient.redeemVoucher(command);
        return true;
    }
}
