package com.poly.customerdataaccess.feign;

import com.poly.customerapplicationservice.command.RedeemVoucherCommand;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "promotion-management", path = "/api/v1/promotions")
public interface PromotionFeignClient {

    @PostMapping
    boolean redeemVoucher(RedeemVoucherCommand command);
}
