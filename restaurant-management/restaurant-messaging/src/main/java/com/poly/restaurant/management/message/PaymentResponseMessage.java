package com.poly.restaurant.management.message;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponseMessage {

    private String id;
    private String orderId;
    private String orderPaymentStatus;
    private String paymentMethod;
}
