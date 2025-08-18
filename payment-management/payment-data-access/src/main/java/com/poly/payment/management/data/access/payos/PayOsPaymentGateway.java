package com.poly.payment.management.data.access.payos;

import com.poly.payment.management.domain.dto.request.CreatePaymentLinkCommand;
import com.poly.payment.management.domain.service.PaymentGateway;
import com.poly.payment.management.domain.dto.CheckoutResponseData;
import com.poly.payment.management.data.access.mapper.PayOSMapper;
import org.springframework.stereotype.Component;
import vn.payos.PayOS;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;

import java.util.List;

@Component
public class PayOsPaymentGateway implements PaymentGateway {

    private static final String RETURN_URL = "https://nika.id.vn/return";

    private static final String CANCEL_URL = "https://nika.id.vn/cancel";

    private final PayOS payOS;

    public PayOsPaymentGateway(PayOS payOS) {
        this.payOS = payOS;
    }

    @Override
    public CheckoutResponseData createPaymentLink(CreatePaymentLinkCommand command) throws Exception {
        List<ItemData> items = command.getItems().stream()
                .map(item -> ItemData.builder()
                        .name(item.getName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice().intValue())
                        .build())
                .toList();

        PaymentData paymentData = PaymentData
                .builder()
                .orderCode(command.getOrderCode())
                .amount(Integer.valueOf(command.getAmount().toString()))
                .description(command.getDescription())
                .returnUrl(RETURN_URL)
                .cancelUrl(CANCEL_URL)
                .items(items)
                .build();

        return PayOSMapper.toCheckoutResponseData(payOS.createPaymentLink(paymentData));
    }

    public void cancelPaymentLink(long referenceCode, String notice) throws Exception {
        payOS.cancelPaymentLink(referenceCode, notice);
    }
}
