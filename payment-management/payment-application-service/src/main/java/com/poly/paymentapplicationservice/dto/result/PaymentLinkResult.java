package com.poly.paymentapplicationservice.dto.result;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class PaymentLinkResult {
    private @NonNull Long orderCode;
    private @NonNull String paymentLinkId;
    private @NonNull String status;
}
