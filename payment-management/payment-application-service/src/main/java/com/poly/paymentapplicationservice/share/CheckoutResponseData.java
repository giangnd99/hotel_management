package com.poly.paymentapplicationservice.share;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CheckoutResponseData {
    private @NonNull String bin;
    private @NonNull String accountNumber;
    private @NonNull String accountName;
    private @NonNull Integer amount;
    private @NonNull String description;
    private @NonNull Long orderCode;
    private @NonNull String currency;
    private @NonNull String paymentLinkId;
    private @NonNull String status;
    private Long expiredAt;
    private @NonNull String checkoutUrl;
    private @NonNull String qrCode;
}
