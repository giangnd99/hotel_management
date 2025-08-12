package com.poly.restaurant.management.message;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestMessage {

    private String id;
    private String orderId;
    private String amount;
    private String orderPaymentStatus;
    private String paymentMethod;
}
