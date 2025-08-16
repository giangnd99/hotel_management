package com.poly.paymentapplicationservice.share;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CheckoutResponseData {
    private String bin;
    private String accountNumber;
    private  String accountName;
    private  Integer amount;
    private  String description;
    private  Long orderCode;
    private  String currency;
    private  String paymentLinkId;
    private  String status;
    private Long expiredAt;
    private  String checkoutUrl;
    private  String qrCode;
}
