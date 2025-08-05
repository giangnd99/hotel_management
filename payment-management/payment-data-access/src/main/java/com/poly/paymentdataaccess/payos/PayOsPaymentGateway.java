package com.poly.paymentdataaccess.payos;

import com.poly.paymentapplicationservice.command.CreateDepositPaymentLinkConmand;
import com.poly.paymentapplicationservice.command.CreatePaymentLinkConmand;
import com.poly.paymentapplicationservice.port.input.PaymentUsecase;
import com.poly.paymentapplicationservice.port.output.PaymentGateway;
import com.poly.paymentapplicationservice.share.CheckoutResponseData;
import com.poly.paymentdataaccess.mapper.PayOSMapper;
import com.poly.paymentdomain.model.entity.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
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
    public CheckoutResponseData createDepositPaymentLink(CreateDepositPaymentLinkConmand command) throws Exception {
        List<vn.payos.type.ItemData> itemData = command.getItems()
                .stream()
                .map(item -> vn.payos.type.ItemData.builder()
                        .name(item.getName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build())
                .toList();

        PaymentData paymentData = PaymentData
                .builder()
                .orderCode(command.getReferenceCode())
                .amount(Integer.valueOf(command.getAmount().toString()))
                .description(command.getDescription())
                .returnUrl(RETURN_URL)
                .cancelUrl(CANCEL_URL)
                .items(itemData)
                .build();

        return PayOSMapper.toCheckoutResponseData(payOS.createPaymentLink(paymentData));
    }

    @Override
    public CheckoutResponseData createPaymentLink(CreatePaymentLinkConmand command) throws Exception {
        List<ItemData> items = command.getItems().stream()
                .map(item -> ItemData.builder()
                        .name(item.getName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build())
                .toList();

        PaymentData paymentData = PaymentData
                .builder()
                .orderCode(command.getPaymentId().node())
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
