package com.poly.paymentapplicationservice.dto.result;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

@Getter
@Builder
public class PaymentLinkResult {
    private UUID paymentId;
    private @NonNull Long orderCode;
    private @NonNull String paymentLink;
    private @NonNull String status;
}
