package com.poly.payment.management.data.access.mapper;

import com.poly.payment.management.domain.dto.CheckoutResponseData;

public class PayOSMapper {
    public static CheckoutResponseData toCheckoutResponseData(vn.payos.type.CheckoutResponseData checkoutPayos){
        return CheckoutResponseData.builder()
                .bin(checkoutPayos.getBin())
                .accountNumber(checkoutPayos.getAccountNumber())
                .accountName(checkoutPayos.getAccountName())
                .amount(checkoutPayos.getAmount())
                .description(checkoutPayos.getDescription())
                .orderCode(checkoutPayos.getOrderCode())
                .currency(checkoutPayos.getCurrency())
                .paymentLinkId(checkoutPayos.getPaymentLinkId())
                .status(checkoutPayos.getStatus())
                .expiredAt(checkoutPayos.getExpiredAt())
                .checkoutUrl(checkoutPayos.getCheckoutUrl())
                .qrCode(checkoutPayos.getQrCode())
                .build();
    }
}
