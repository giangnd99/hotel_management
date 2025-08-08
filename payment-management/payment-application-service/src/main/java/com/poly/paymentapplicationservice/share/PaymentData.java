package com.poly.paymentapplicationservice.share;

import lombok.Builder;
import lombok.NonNull;

import java.util.List;

@Builder
public class PaymentData {
    private @NonNull Long orderCode;
    private @NonNull Integer amount;
    private @NonNull String description;
    private List<ItemData> items;
    private @NonNull String cancelUrl;
    private @NonNull String returnUrl;
    private String signature;
    private String buyerName;
    private String buyerEmail;
    private String buyerPhone;
    private String buyerAddress;
    private Long expiredAt;
}
